package org.fgai4h.ap.domain.dataset.service;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.domain.dataset.entity.DatasetRoleEntity;
import org.fgai4h.ap.domain.dataset.mapper.DatasetRoleApiMapper;
import org.fgai4h.ap.domain.dataset.mapper.DatasetRoleMapper;
import org.fgai4h.ap.domain.dataset.mapper.DatasetRoleModelAssembler;
import org.fgai4h.ap.domain.dataset.model.DatasetModel;
import org.fgai4h.ap.domain.dataset.model.DatasetRoleModel;
import org.fgai4h.ap.domain.dataset.repository.DatasetRoleRepository;
import org.fgai4h.ap.domain.error.DomainError;
import org.fgai4h.ap.domain.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DatasetRoleService {

    private final DatasetRoleRepository datasetRoleRepository;
    private final DatasetRoleModelAssembler datasetModelAssembler;
    private final DatasetRoleModelAssembler datasetRoleModelAssembler;
    private final DatasetRoleMapper datasetRoleMapper;

    public DatasetRoleModel addDatasetRole(DatasetRoleModel datasetRoleModel) {
        DatasetRoleEntity datasetRoleEntity = datasetRoleRepository.save(datasetRoleMapper.toDatasetRoleEntity(datasetRoleModel));
        return datasetModelAssembler.toModel(datasetRoleEntity);
    }

    public void updateDatasetRole(DatasetRoleModel datasetRoleModel) {
        datasetRoleRepository.save(datasetRoleMapper.toDatasetRoleEntity(datasetRoleModel));
    }

    public void deleteRolesByDatasetId(UUID datasetId) {
        List<DatasetRoleEntity> roles = datasetRoleRepository.findRolesByDatasetId(datasetId);
        roles.stream().forEach(r -> datasetRoleRepository.deleteById(r.getDatasetRoleUUID()));
    }

    public List<DatasetRoleModel> getDatasetRolesForDatasetId(UUID datasetId){
        return datasetRoleRepository.findRolesByDatasetId(datasetId).stream().map(datasetRoleModelAssembler::toModel).collect(Collectors.toList());
    }

    public void deleteDatasetRoleById(UUID datasetRoleId) {
        datasetRoleRepository.deleteById(datasetRoleId);
    }

    public Optional<DatasetRoleModel> getDatasetRoleById(UUID datasetRoleId) {
        return Optional.ofNullable(datasetRoleRepository
                .findById(datasetRoleId)
                .map(datasetRoleModelAssembler::toModel)
                .orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, "DatasetRole", "id", datasetRoleId)));
    }
}
