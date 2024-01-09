package org.fgai4h.ap.domain.dataset.mapper;

import org.fgai4h.ap.api.model.DatasetRoleDto;
import org.fgai4h.ap.domain.dataset.model.DatasetModel;
import org.fgai4h.ap.domain.dataset.model.DatasetRoleModel;
import org.fgai4h.ap.domain.dataset.service.DatasetService;
import org.fgai4h.ap.domain.user.mapper.UserApiMapper;
import org.fgai4h.ap.domain.user.service.UserService;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = {UserApiMapper.class, DatasetApiMapper.class, DatasetService.class, UserService.class})
public interface DatasetRoleApiMapper {

    @Mapping(source = "id", target = "datasetRoleUUID")
    DatasetRoleModel toDatasetRoleModel(DatasetRoleDto datasetDto);

    List<DatasetModel> toListDatasetRoleModel(List<UUID> value);
    List<UUID> toListUUID(List<DatasetRoleModel> value);

    default UUID map(DatasetRoleModel value){
        return value.getDatasetRoleUUID();
    }


    @InheritInverseConfiguration
    @Mapping(source="user.username", target="username")
    DatasetRoleDto toDatasetRoleDto(final DatasetRoleModel datasetModel);
}
