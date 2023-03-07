package org.fgai4h.ap.domain.dataset.mapper;

import org.fgai4h.ap.api.model.DatasetDto;
import org.fgai4h.ap.domain.dataset.model.DatasetModel;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = {DatasetMetadataApiMapper.class})
public interface DatasetApiMapper {

    @Mapping(source = "id", target = "datasetUUID")
    DatasetModel toDatasetModel(DatasetDto datasetDto);

    List<DatasetModel> toListDatasetModel(List<UUID> value);
    List<UUID> toListUUID(List<DatasetModel> value);

    @Mapping(source = "value", target = "datasetUUID")
    DatasetModel map(UUID value);

    default UUID map(DatasetModel value){
        return value.getDatasetUUID();
    }


    @InheritInverseConfiguration
    DatasetDto toDatasetDto(final DatasetModel datasetModel);
}
