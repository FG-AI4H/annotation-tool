package org.fgai4h.ap.domain.catalog.mapper;

import org.fgai4h.ap.api.model.DataCatalogDto;
import org.fgai4h.ap.domain.catalog.model.DataCatalogModel;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DataCatalogApiMapper {

    @Mapping(source = "id", target = "dataCatalogUUID")
    DataCatalogModel toDataCatalogModel(DataCatalogDto annotationDataDto);

    @InheritInverseConfiguration
    DataCatalogDto toDataCatalogDto(final DataCatalogModel annotationDataModel);

}