package org.fgai4h.ap.domain.campaign.mapper;

import org.fgai4h.ap.domain.campaign.controller.CampaignController;
import org.fgai4h.ap.domain.campaign.entity.CampaignEntity;
import org.fgai4h.ap.domain.campaign.model.CampaignModel;
import org.fgai4h.ap.domain.dataset.entity.DatasetEntity;
import org.fgai4h.ap.domain.dataset.mapper.DatasetMapper;
import org.fgai4h.ap.domain.dataset.model.DatasetModel;
import org.fgai4h.ap.domain.user.entity.UserEntity;
import org.fgai4h.ap.domain.user.mapper.AnnotatorModelAssembler;
import org.fgai4h.ap.domain.user.mapper.ReviewerModelAssembler;
import org.fgai4h.ap.domain.user.mapper.SupervisorModelAssembler;
import org.fgai4h.ap.domain.user.mapper.UserMapper;
import org.fgai4h.ap.domain.user.model.UserModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CampaignModelAssembler extends RepresentationModelAssemblerSupport<CampaignEntity, CampaignModel> {

    public CampaignModelAssembler() {
        super(CampaignController.class, CampaignModel.class);
    }

    @Override
    public CampaignModel toModel(CampaignEntity entity)
    {
        CampaignModel campaignModel = instantiateModel(entity);

        if (isNull(entity))
            return campaignModel;

        campaignModel.add(linkTo(
                methodOn(CampaignController.class)
                        .getCampaignById(entity.getCampaignUUID()))
                .withSelfRel());

        campaignModel.setCampaignUUID(entity.getCampaignUUID());
        campaignModel.setName(entity.getName());
        campaignModel.setDescription(entity.getDescription());
        campaignModel.setStatus(entity.getStatus());
        campaignModel.setDatasets(toDatasetModel(entity.getDatasets()));
        campaignModel.setAnnotators(toUserModel(entity.getAnnotators()));
        campaignModel.setReviewers(toUserModel(entity.getReviewers()));
        campaignModel.setSupervisors(toUserModel(entity.getSupervisors()));
        campaignModel.setAnnotationKind(entity.getAnnotationKind());
        campaignModel.setAnnotationTool(entity.getAnnotationTool());
        campaignModel.setAnnotationMethod(entity.getAnnotationMethod());
        campaignModel.setAnnotationInstructions(entity.getAnnotationInstructions());
        return campaignModel;
    }

    private List<DatasetModel> toDatasetModel(List<DatasetEntity> datasets) {
        if (isNull(datasets) || datasets.isEmpty())
            return Collections.emptyList();

        return datasets.stream()
                .map(dataset-> DatasetModel.builder()
                        .datasetUUID(dataset.getDatasetUUID())
                        .name(dataset.getName())
                        .description(dataset.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    private List<UserModel> toUserModel(List<UserEntity> users) {
        if (isNull(users) || users.isEmpty())
            return Collections.emptyList();

        AnnotatorModelAssembler annotatorModelAssembler = new AnnotatorModelAssembler();
        ReviewerModelAssembler reviewerModelAssembler = new ReviewerModelAssembler();
        SupervisorModelAssembler supervisorModelAssembler = new SupervisorModelAssembler();

        return users.stream()
                .map(user-> UserModel.builder()
                        .userUUID(user.getUserUUID())
                        .username(user.getUsername())
                        .idpID(user.getIdpID())
                        .annotatorRole(annotatorModelAssembler.toModel(user.getAnnotatorRole()))
                        .supervisorRole(supervisorModelAssembler.toModel(user.getSupervisorRole()))
                        .reviewerRole(reviewerModelAssembler.toModel(user.getReviewerRole()))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public CollectionModel<CampaignModel> toCollectionModel(Iterable<? extends CampaignEntity> entities)
    {
        CollectionModel<CampaignModel> campaignModels = super.toCollectionModel(entities);

        campaignModels.add(linkTo(methodOn(CampaignController.class).getAllCampaigns()).withSelfRel());

        return campaignModels;
    }

    public CampaignEntity toEntity(CampaignModel campaignModel) {

        AnnotatorModelAssembler annotatorModelAssembler = new AnnotatorModelAssembler();
        ReviewerModelAssembler reviewerModelAssembler = new ReviewerModelAssembler();
        SupervisorModelAssembler supervisorModelAssembler = new SupervisorModelAssembler();

        return CampaignEntity.builder()
                .annotationInstructions(campaignModel.getAnnotationInstructions())
                .name(campaignModel.getName())
                .description(campaignModel.getDescription())
                .annotationKind(campaignModel.getAnnotationKind())
                .campaignUUID(campaignModel.getCampaignUUID())
                .annotationMethod(campaignModel.getAnnotationMethod())
                .annotationTool(campaignModel.getAnnotationTool())
                .status(campaignModel.getStatus())
                .datasets(DatasetMapper.INSTANCE.toListDatasetEntity(campaignModel.getDatasets()))
                .annotators(UserMapper.INSTANCE.toListUserEntity(campaignModel.getAnnotators()))
                .reviewers(UserMapper.INSTANCE.toListUserEntity(campaignModel.getReviewers()))
                .supervisors(UserMapper.INSTANCE.toListUserEntity(campaignModel.getSupervisors()))
                .build();
    }
}
