package org.fgai4h.ap.domain.campaign.service;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.api.model.CampaignStatus;
import org.fgai4h.ap.domain.campaign.entity.CampaignEntity;
import org.fgai4h.ap.domain.campaign.mapper.CampaignMapper;
import org.fgai4h.ap.domain.campaign.mapper.CampaignModelAssembler;
import org.fgai4h.ap.domain.campaign.model.CampaignModel;
import org.fgai4h.ap.domain.campaign.model.CampaignStatusModel;
import org.fgai4h.ap.domain.campaign.repository.CampaignRepository;
import org.fgai4h.ap.domain.error.DomainError;
import org.fgai4h.ap.domain.exception.NotFoundException;
import org.fgai4h.ap.domain.task.entity.AnnotationTaskEntity;
import org.fgai4h.ap.domain.task.entity.SampleEntity;
import org.fgai4h.ap.domain.task.entity.TaskEntity;
import org.fgai4h.ap.domain.task.model.TaskKind;
import org.fgai4h.ap.domain.task.repository.AnnotationTaskRepository;
import org.fgai4h.ap.domain.task.repository.TaskRepository;
import org.fgai4h.ap.domain.user.entity.UserEntity;
import org.fgai4h.ap.helpers.AWSS3;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private static final String DOMAIN_NAME = "Campaign";

    private final CampaignRepository campaignRepository;
    private final CampaignModelAssembler campaignModelAssembler;
    private final CampaignMapper campaignMapper;

    private final TaskRepository taskRepository;
    private final AnnotationTaskRepository annotationTaskRepository;

    public List<CampaignModel> getAllCampaigns() {
        return campaignRepository.findAll().stream()
                .map(campaignModelAssembler::toModel).collect(Collectors.toList());
    }

    public CampaignModel addCampaign(@NotNull CampaignModel campaignModel) {
        campaignModel.setStatus(CampaignStatusModel.DRAFT);
        CampaignEntity newCampaign = campaignRepository.save(campaignMapper.toCampaignEntity(campaignModel));
        return campaignModelAssembler.toModel(newCampaign);
    }

    public Optional<CampaignModel> getCampaignById(UUID campaignId) {
        return Optional.ofNullable(campaignRepository
                .findById(campaignId)
                .map(campaignModelAssembler::toModel)
                .orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, DOMAIN_NAME, "id", campaignId)));
    }

    public CampaignModel findById(UUID campaignId){
        return getCampaignById(campaignId).orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, DOMAIN_NAME, "id", campaignId));
    }

    public void updateCampaign(CampaignModel campaignModel) {
        campaignRepository.save(campaignMapper.toCampaignEntity(campaignModel));
    }

    public void startCampaign(UUID campaignId) {
        CampaignEntity campaign = campaignRepository.findById(campaignId).orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, DOMAIN_NAME, "id", campaignId));
        campaign.setStatus(CampaignStatus.RUNNING.toString());
        campaignRepository.save(campaign);
    }

    public void deleteCampaignById(UUID campaignId) {
        campaignRepository.deleteById(campaignId);
    }

    public void generateTasks(UUID campaignId) {
        CampaignEntity campaign = campaignRepository.findById(campaignId).orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, DOMAIN_NAME, "id", campaignId));

        //Create annotation task for the campaign
        List<AnnotationTaskEntity> annotationTaskList = new ArrayList<>();
        annotationTaskList.add(AnnotationTaskEntity.builder()
                .kind(campaign.getAnnotationKind()).build());

        campaign.getDatasets().forEach(datasetEntity -> {
            //Get sample reference
            S3Client s3client = S3Client.builder()
                    .region(Region.EU_CENTRAL_1)
                    //.credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                    .build();

            List<S3Object> objects = AWSS3.listBucketObjects(s3client,"fhir-service-dev-fhirbinarybucket-yjeth32swz5m",datasetEntity.getStorageLocation().replaceAll("fhir-service-dev-fhirbinarybucket-yjeth32swz5m.s3.eu-central-1.amazonaws.com/", ""));
            s3client.close();

            List<AnnotationTaskEntity> annotationTaskEntityList = new ArrayList<>();
            annotationTaskEntityList.add(annotationTaskRepository.save(AnnotationTaskEntity.builder()
                    .kind(campaign.getAnnotationKind())
                    .title(campaign.getAnnotationKind())
                    .build()));

            for (int i = 0; i < objects.size(); i++) {

                S3Object myValue = objects.get(i);
                if(myValue.size() > 0){

                    for (int j = 0; j < campaign.getMinAnnotation(); j++) {
                        UserEntity assignee = campaign.getAnnotators().get((i+j) % campaign.getAnnotators().size());

                        TaskEntity taskEntity = TaskEntity.builder()
                                .campaign(campaign)
                                .kind(TaskKind.CREATE.toString())
                                .assignee(assignee)
                                .annotationTasks(annotationTaskEntityList)
                                .readOnly(false)
                                .build();

                        List<SampleEntity> samples = new ArrayList<>();

                        samples.add(SampleEntity.builder()
                                .task(taskEntity)
                                .data(myValue.key())
                                .title(myValue.key() + ": " + myValue.eTag())
                                .build());

                        taskEntity.setSamples(samples);
                        taskRepository.save(taskEntity);
                    }
                }
            }
        });
    }
}
