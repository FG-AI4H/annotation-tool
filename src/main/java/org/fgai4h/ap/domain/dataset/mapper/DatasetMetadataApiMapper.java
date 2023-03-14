package org.fgai4h.ap.domain.dataset.mapper;

import org.fgai4h.ap.api.model.DatasetMetadataDto;
import org.fgai4h.ap.domain.dataset.model.DatasetMetadataModel;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DatasetMetadataApiMapper {

    @Mapping(source = "id", target = "metadataUUID")
    DatasetMetadataModel toDatasetMetadataModel(DatasetMetadataDto datasetMetadataDto);

    @InheritInverseConfiguration
    DatasetMetadataDto toDatasetMetadataDto(final DatasetMetadataModel datasetMetadataModel);
}
