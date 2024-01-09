package org.fgai4h.ap.domain.catalog.service;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.domain.catalog.entity.DataCatalogEntity;
import org.fgai4h.ap.domain.catalog.mapper.DataAccessRequestMapper;
import org.fgai4h.ap.domain.catalog.mapper.DataAccessRequestModelAssembler;
import org.fgai4h.ap.domain.catalog.mapper.DataCatalogMapper;
import org.fgai4h.ap.domain.catalog.mapper.DataCatalogModelAssembler;
import org.fgai4h.ap.domain.catalog.model.DataAccessRequestModel;
import org.fgai4h.ap.domain.catalog.model.DataCatalogModel;
import org.fgai4h.ap.domain.catalog.repository.DataAccessRequestRepository;
import org.fgai4h.ap.domain.catalog.repository.DataCatalogRepository;
import org.fgai4h.ap.domain.dataset.model.DatasetCatalogRequestStatusModel;
import org.fgai4h.ap.domain.error.DomainError;
import org.fgai4h.ap.domain.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataCatalogService {

    private static final String DOMAIN_NAME = "Catalog";

    private final DataCatalogRepository dataCatalogRepository;
    private final DataAccessRequestRepository dataAccessRequestRepository;
    private final DataCatalogModelAssembler dataCatalogModelAssembler;
    private final DataAccessRequestModelAssembler dataAccessRequestModelAssembler;
    private final DataCatalogMapper dataCatalogMapper;
    private final DataAccessRequestMapper dataAccessRequestMapper;

    public List<DataCatalogModel> getDataCatalogs(String userUUID) {
        return dataCatalogRepository.findAll().stream()
                .map(dataCatalogModelAssembler::toModel).collect(Collectors.toList());
    }

    public DataCatalogModel addDataCatalog(DataCatalogModel dataCatalogModel) {
        DataCatalogEntity newDataCatalog = dataCatalogRepository.save(dataCatalogMapper.toDataCatalogEntity(dataCatalogModel));
        return dataCatalogModelAssembler.toModel(newDataCatalog);
    }

    public Optional<DataCatalogModel> getDataCatalogById(UUID catalogId) {
        return Optional.ofNullable(dataCatalogRepository
                .findById(catalogId)
                .map(dataCatalogModelAssembler::toModel)
                .orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, DOMAIN_NAME, "id", catalogId)));
    }

    public void updateDataCatalog(DataCatalogModel dataCatalogModel) {
        dataCatalogRepository.save(dataCatalogMapper.toDataCatalogEntity(dataCatalogModel));
    }

    public void deleteDataCatalogById(UUID dataCatalogId) {
        dataCatalogRepository.deleteById(dataCatalogId);
    }

    public Optional<DataAccessRequestModel> getDataAccessRequestById(UUID dataAccessRequestId) {
        return Optional.ofNullable(dataAccessRequestRepository
                .findById(dataAccessRequestId)
                .map(dataAccessRequestModelAssembler::toModel)
                .orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, DOMAIN_NAME, "id", dataAccessRequestId)));
    }

    public List<DataAccessRequestModel> getOwnerDataAccessRequests(String userUUID) {
        return dataAccessRequestRepository.findAllByOwnerId(userUUID)
                .stream()
                .map(dataAccessRequestModelAssembler::toModel).collect(Collectors.toList());
    }

    public List<DataAccessRequestModel> getRequesterDataAccessRequests(String userUUID) {
        return dataAccessRequestRepository.findAllByRequesterId(userUUID)
                .stream()
                .map(dataAccessRequestModelAssembler::toModel).collect(Collectors.toList());
    }

    public void deleteDataAccessRequestById(UUID dataAccessRequestId) {
        dataAccessRequestRepository.deleteById(dataAccessRequestId);
    }

    public void updateDataAccessRequest(DataAccessRequestModel dataAccessRequestModel) {
        dataAccessRequestRepository.save(dataAccessRequestMapper.toDataAccessRequestEntity(dataAccessRequestModel));
    }

    public void updateDataAccessRequestStatus(UUID dataAccessRequestId, String status) {
        getDataAccessRequestById(dataAccessRequestId).ifPresent(d -> {
            d.setRequestStatus(DatasetCatalogRequestStatusModel.valueOf(status));
            d.setRequestStatusDate(LocalDateTime.now());
            dataAccessRequestRepository.save(dataAccessRequestMapper.toDataAccessRequestEntity(d));
        });
    }
}
