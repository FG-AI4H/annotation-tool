package org.fgai4h.ap.domain.catalog.mapper;

import org.fgai4h.ap.domain.catalog.controller.DataCatalogController;
import org.fgai4h.ap.domain.catalog.entity.DataAccessRequestEntity;
import org.fgai4h.ap.domain.catalog.model.DataAccessRequestModel;
import org.fgai4h.ap.domain.dataset.mapper.DatasetModelAssembler;
import org.fgai4h.ap.domain.dataset.model.DatasetCatalogRequestStatusModel;
import org.fgai4h.ap.domain.user.mapper.UserModelAssembler;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class DataAccessRequestModelAssembler extends RepresentationModelAssemblerSupport<DataAccessRequestEntity, DataAccessRequestModel> {

    public DataAccessRequestModelAssembler() {
        super(DataCatalogController.class, DataAccessRequestModel.class);
    }

    @Override
    public DataAccessRequestModel toModel(DataAccessRequestEntity entity) {
        DataAccessRequestModel dataAccessRequestModel = instantiateModel(entity);
        DatasetModelAssembler datasetModelAssembler = new DatasetModelAssembler();

        if(entity == null){
            return dataAccessRequestModel;
        }

        UserModelAssembler userModelAssembler = new UserModelAssembler();

        dataAccessRequestModel.add(linkTo(
                methodOn(DataCatalogController.class)
                        .getDataCatalogById(entity.getDataAccessRequestUUID()))
                .withSelfRel());

        dataAccessRequestModel.setDataAccessRequestUUID(entity.getDataAccessRequestUUID());
        dataAccessRequestModel.setRequestDate(entity.getRequestDate());
        dataAccessRequestModel.setRequestStatus(entity.getRequestStatus() != null ? DatasetCatalogRequestStatusModel.valueOf(entity.getRequestStatus()):null);
        dataAccessRequestModel.setDataOwner(userModelAssembler.toModel(entity.getDataOwner()));
        dataAccessRequestModel.setRequester(userModelAssembler.toModel(entity.getRequester()));
        dataAccessRequestModel.setDataset(datasetModelAssembler.toModel(entity.getDataset()));

        return dataAccessRequestModel;
    }
}
