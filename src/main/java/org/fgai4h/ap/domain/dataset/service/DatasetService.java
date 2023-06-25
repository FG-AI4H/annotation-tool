package org.fgai4h.ap.domain.dataset.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.api.model.DatasetDto;
import org.fgai4h.ap.api.model.DatasetMetadataDto;
import org.fgai4h.ap.domain.catalog.entity.DataAccessRequestEntity;
import org.fgai4h.ap.domain.catalog.mapper.DataAccessRequestMapper;
import org.fgai4h.ap.domain.catalog.model.DataAccessRequestModel;
import org.fgai4h.ap.domain.catalog.model.DataCatalogModel;
import org.fgai4h.ap.domain.catalog.repository.DataAccessRequestRepository;
import org.fgai4h.ap.domain.catalog.service.DataCatalogService;
import org.fgai4h.ap.domain.dataset.entity.DatasetEntity;
import org.fgai4h.ap.domain.dataset.mapper.DatasetMapper;
import org.fgai4h.ap.domain.dataset.mapper.DatasetModelAssembler;
import org.fgai4h.ap.domain.dataset.model.DatasetCatalogRequestStatusModel;
import org.fgai4h.ap.domain.dataset.model.DatasetModel;
import org.fgai4h.ap.domain.dataset.model.DatasetRoleModel;
import org.fgai4h.ap.domain.dataset.repository.DatasetRepository;
import org.fgai4h.ap.domain.error.DomainError;
import org.fgai4h.ap.domain.exception.NotFoundException;
import org.fgai4h.ap.domain.user.model.UserModel;
import org.fgai4h.ap.domain.user.model.UserRole;
import org.fgai4h.ap.domain.user.service.UserService;
import org.fgai4h.ap.helpers.AWSAthena;
import org.fgai4h.ap.helpers.AWSGlue;
import org.fgai4h.ap.helpers.AWSS3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DatasetService {

    static Logger logger = LoggerFactory.getLogger(DatasetService.class);

    private final DatasetRepository datasetRepository;
    private final DataAccessRequestRepository dataAccessRequestRepository;
    private final DatasetModelAssembler datasetModelAssembler;
    private final DatasetMapper datasetMapper;
    private final DataAccessRequestMapper dataAccessRequestMapper;
    private final DatasetRoleService datasetRoleService;
    private final UserService userService;
    private final DataCatalogService dataCatalogService;

    public List<DatasetModel> getAllDatasets(String userUUID) {
        List<DatasetModel> allDatasets = datasetRepository.findAllByUserId(userUUID).stream()
                .map(datasetModelAssembler::toModel).collect(Collectors.toList());
        allDatasets.forEach(e-> {
            DataAccessRequestEntity dataAccessRequestEntity = dataAccessRequestRepository.findByDatasetId(e.getDatasetUUID());
            if(dataAccessRequestEntity != null) {
                e.setRequestStatus(DatasetCatalogRequestStatusModel.valueOf(dataAccessRequestEntity.getRequestStatus()));
            }
        });

        return allDatasets;
    }

    public DatasetModel addDataset(DatasetModel datasetModel, String userIdpId) {
        DatasetEntity newDataset = datasetRepository.save(datasetMapper.toDatasetEntity(datasetModel));

        UserModel currentUser = userService.getUserByIdpId(userIdpId).orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, "User", "idpId", userIdpId));

        DatasetRoleModel datasetRole = new DatasetRoleModel();
        datasetRole.setDataset(datasetModelAssembler.toModel(newDataset));
        datasetRole.setUserRole(UserRole.OWNER);
        datasetRole.setUser(currentUser);

        datasetRoleService.addDatasetRole(datasetRole);

        return datasetModelAssembler.toModel(newDataset);
    }

    public Optional<DatasetModel> getDatasetById(UUID datasetId) {
        return Optional.ofNullable(datasetRepository
                .findById(datasetId)
                .map(datasetModelAssembler::toModel)
                .orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, "Dataset", "id", datasetId)));
    }

    public DatasetModel findById(UUID datasetId) {
        return getDatasetById(datasetId).orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, "Dataset", "id", datasetId));
    }

    public void updateDataset(DatasetModel datasetModel) {
        datasetRepository.save(datasetMapper.toDatasetEntity(datasetModel));
    }

    public void deleteCampaignById(UUID datasetId) {
        datasetRoleService.deleteRolesByDatasetId(datasetId);
        datasetRepository.deleteById(datasetId);
    }

    public List<DatasetDto> getCatalogDatasets(String userUUID){
        List<DataCatalogModel> allCatalogs = dataCatalogService.getDataCatalogs(userUUID);
        List<DatasetDto> datasetDtoList = new ArrayList<>();
        allCatalogs.forEach(e-> {
                    List<DatasetDto> datasetDtos = AWSAthena.getCatalogDatasets(e);
                    datasetDtos.forEach(c->
                            userService.getUserById(c.getMetadata().getDataOwnerId()).ifPresent(u -> c.getMetadata().setDataOwnerName(u.getUsername())));
                    datasetDtoList.addAll(datasetDtos);
                });

        return datasetDtoList;
    }

    private String metatdataObjectToJson(DatasetMetadataDto datasetMetadataDto){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        try {
            return objectMapper.writeValueAsString(datasetMetadataDto);
        } catch (JsonProcessingException e) {
            logger.info(e.getMessage());
            return null;
        }
    }

    public void writeMetadataToCatalog(DatasetDto datasetDto) {
        //Write Metatdata to data catalog
        if (datasetDto.getDataCatalogId() != null) {
            DataCatalogModel dataCatalogModel = dataCatalogService.getDataCatalogById(datasetDto.getDataCatalogId()).get();
            AWSS3.putObject(
                    dataCatalogModel,
                    "text/plain",
                    datasetDto.getName(),
                    metatdataObjectToJson(datasetDto.getMetadata()).getBytes(StandardCharsets.UTF_8));

            AWSGlue.startSpecificCrawler(dataCatalogModel,"fgai4h-oci-data-hub-"+dataCatalogModel.getAwsRegion()+"-crawler");
        }
    }

    public DataAccessRequestEntity addDataAccessRequest(DataAccessRequestModel dataAccessRequestModel) {
        return dataAccessRequestRepository.save(dataAccessRequestMapper.toDataAccessRequestEntity(dataAccessRequestModel));
    }
}
