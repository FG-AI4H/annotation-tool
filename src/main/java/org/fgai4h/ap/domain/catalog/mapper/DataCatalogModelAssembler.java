package org.fgai4h.ap.domain.catalog.mapper;

import org.fgai4h.ap.domain.catalog.controller.DataCatalogController;
import org.fgai4h.ap.domain.catalog.entity.DataCatalogEntity;
import org.fgai4h.ap.domain.catalog.model.DataCatalogModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DataCatalogModelAssembler extends RepresentationModelAssemblerSupport<DataCatalogEntity, DataCatalogModel> {

    public DataCatalogModelAssembler() {
        super(DataCatalogController.class, DataCatalogModel.class);
    }

    @Override
    public DataCatalogModel toModel(DataCatalogEntity entity) {
        DataCatalogModel taskModel = instantiateModel(entity);

        taskModel.add(linkTo(
                methodOn(DataCatalogController.class)
                        .getDataCatalogById(entity.getDataCatalogUUID()))
                .withSelfRel());

        taskModel.setDataCatalogUUID(entity.getDataCatalogUUID());
        taskModel.setName(entity.getName());
        taskModel.setDescription(entity.getDescription());
        taskModel.setProvider(entity.getProvider());
        taskModel.setProviderCatalogId(entity.getProviderCatalogId());
        taskModel.setAwsRegion(entity.getAwsRegion());
        taskModel.setDatabaseName(entity.getDatabaseName());

        return taskModel;
    }
}
