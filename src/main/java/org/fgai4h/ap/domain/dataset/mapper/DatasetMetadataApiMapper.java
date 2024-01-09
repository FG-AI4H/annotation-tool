package org.fgai4h.ap.domain.dataset.mapper;

import org.fgai4h.ap.api.model.DatasetMetadataDto;
import org.fgai4h.ap.domain.dataset.model.DatasetMetadataModel;
import org.fgai4h.ap.domain.user.mapper.UserApiMapper;
import org.fgai4h.ap.domain.user.service.UserService;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserApiMapper.class, UserService.class})
public interface DatasetMetadataApiMapper {

    @Mapping(source = "id", target = "metadataUUID")
    @Mapping(source = "dataOwnerId", target="dataOwner")
    DatasetMetadataModel toDatasetMetadataModel(DatasetMetadataDto datasetMetadataDto);

    @InheritInverseConfiguration
    DatasetMetadataDto toDatasetMetadataDto(final DatasetMetadataModel datasetMetadataModel);

}
