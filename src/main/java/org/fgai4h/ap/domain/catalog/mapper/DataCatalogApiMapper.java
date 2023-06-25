package org.fgai4h.ap.domain.catalog.mapper;

import org.fgai4h.ap.api.model.DataAccessRequestDto;
import org.fgai4h.ap.api.model.DataCatalogDto;
import org.fgai4h.ap.api.model.TableDto;
import org.fgai4h.ap.domain.catalog.model.DataAccessRequestModel;
import org.fgai4h.ap.domain.catalog.model.DataCatalogModel;
import org.fgai4h.ap.domain.catalog.model.TableModel;
import org.fgai4h.ap.domain.user.mapper.UserApiMapper;
import org.fgai4h.ap.domain.user.service.UserService;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserApiMapper.class, UserService.class})
public interface DataCatalogApiMapper {

    @Mapping(source = "id", target = "dataCatalogUUID")
    DataCatalogModel toDataCatalogModel(DataCatalogDto annotationDataDto);

    @InheritInverseConfiguration
    DataCatalogDto toDataCatalogDto(final DataCatalogModel annotationDataModel);

    @Mapping(source = "id", target = "tableUUID")
    TableModel toTableModel(TableDto tableDto);

    @InheritInverseConfiguration
    TableDto toTableDto(final TableModel tableModel);

    @Mapping(source = "id", target = "dataAccessRequestUUID")
    @Mapping(source = "dataOwnerId", target="dataOwner")
    @Mapping(source = "requesterId", target="requester")
    DataAccessRequestModel toDataAccessRequestModel(DataAccessRequestDto dataAccessRequestDto);

    @InheritInverseConfiguration
    @Mapping(source = "requester.username", target="requesterName")
    @Mapping(source = "dataOwner.username", target="dataOwnerName")
    @Mapping(source = "dataset.name", target="datasetName")
    @Mapping(source = "dataset.datasetUUID", target="datasetId")
    DataAccessRequestDto toDataAccessRequestDto(final DataAccessRequestModel dataAccessRequestModel);

}
