package org.fgai4h.ap.domain.dataset.mapper;

import org.fgai4h.ap.domain.dataset.entity.DatasetEntity;
import org.fgai4h.ap.domain.dataset.model.DatasetModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DatasetMetadataMapper.class})
public interface DatasetMapper {

    DatasetMapper INSTANCE = Mappers.getMapper(DatasetMapper.class);

    DatasetEntity toDatasetEntity(DatasetModel datasetModel);
    List<DatasetEntity> toListDatasetEntity(final List<DatasetModel> datasetModelList);
}
