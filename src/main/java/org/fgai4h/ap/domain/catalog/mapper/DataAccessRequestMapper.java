package org.fgai4h.ap.domain.catalog.mapper;

import org.fgai4h.ap.domain.catalog.entity.DataAccessRequestEntity;
import org.fgai4h.ap.domain.catalog.model.DataAccessRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DataAccessRequestMapper {

    DataAccessRequestMapper INSTANCE = Mappers.getMapper(DataAccessRequestMapper.class);

    DataAccessRequestEntity toDataAccessRequestEntity(DataAccessRequestModel campaignModel);
    List<DataAccessRequestEntity> toListDataAccessRequestEntity(final List<DataAccessRequestModel> dataCatalogModelList);
}
