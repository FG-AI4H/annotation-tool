package org.fgai4h.ap.domain.dataset.service;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.domain.dataset.entity.DatasetRoleEntity;
import org.fgai4h.ap.domain.dataset.mapper.DatasetRoleMapper;
import org.fgai4h.ap.domain.dataset.mapper.DatasetRoleModelAssembler;
import org.fgai4h.ap.domain.dataset.model.DatasetRoleModel;
import org.fgai4h.ap.domain.dataset.repository.DatasetRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public void deleteRolesByDatasetId(UUID datasetId) {
        List<DatasetRoleEntity> roles = datasetRoleRepository.findRolesByDatasetId(datasetId);
        roles.stream().forEach(r -> datasetRoleRepository.deleteById(r.getDatasetRoleUUID()));
    }

    public List<DatasetRoleModel> getDatasetRolesForDatasetId(UUID datasetId){
        return datasetRoleRepository.findRolesByDatasetId(datasetId).stream().map(datasetRoleModelAssembler::toModel).collect(Collectors.toList());
    }
}
