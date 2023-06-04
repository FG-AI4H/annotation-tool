package org.fgai4h.ap.domain.catalog.mapper;

import org.fgai4h.ap.domain.catalog.entity.DataCatalogEntity;
import org.fgai4h.ap.domain.catalog.model.DataCatalogModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DataCatalogMapper {

    org.fgai4h.ap.domain.catalog.mapper.DataCatalogMapper INSTANCE = Mappers.getMapper(org.fgai4h.ap.domain.catalog.mapper.DataCatalogMapper.class);

    DataCatalogEntity toDataCatalogEntity(DataCatalogModel campaignModel);
    List<DataCatalogEntity> toListDataCatalogEntity(final List<DataCatalogModel> dataCatalogModelList);
}
