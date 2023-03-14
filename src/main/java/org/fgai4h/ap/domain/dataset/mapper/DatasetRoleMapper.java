package org.fgai4h.ap.domain.dataset.mapper;

import org.fgai4h.ap.domain.dataset.entity.DatasetRoleEntity;
import org.fgai4h.ap.domain.dataset.model.DatasetRoleModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DatasetRoleMapper {

    DatasetRoleMapper INSTANCE = Mappers.getMapper(DatasetRoleMapper.class);

    DatasetRoleEntity toDatasetRoleEntity(DatasetRoleModel datasetRoleModel);
    List<DatasetRoleEntity> toListDatasetRoleEntity(final List<DatasetRoleModel> datasetRoleModelList);
}
