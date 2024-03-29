package org.fgai4h.ap.domain.dataset.mapper;

import org.fgai4h.ap.domain.dataset.entity.DatasetMetadataEntity;
import org.fgai4h.ap.domain.dataset.model.DatasetMetadataModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DatasetMetadataMapper {

    DatasetMetadataMapper INSTANCE = Mappers.getMapper(DatasetMetadataMapper.class);

    DatasetMetadataEntity toDatasetMetadataEntity(DatasetMetadataModel datasetMetadataModel);
    List<DatasetMetadataEntity> toListDatasetMetadataEntity(final List<DatasetMetadataModel> datasetMetadataModelList);
}
