package org.fgai4h.ap.domain.dataset.mapper;

import org.fgai4h.ap.domain.dataset.controller.DatasetController;
import org.fgai4h.ap.domain.dataset.entity.DatasetEntity;
import org.fgai4h.ap.domain.dataset.model.DatasetModel;
import org.fgai4h.ap.domain.dataset.model.DatasetVisibility;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DatasetModelAssembler extends RepresentationModelAssemblerSupport<DatasetEntity, DatasetModel> {

    public DatasetModelAssembler() {
        super(DatasetController.class, DatasetModel.class);
    }

    @Override
    public DatasetModel toModel(DatasetEntity entity)
    {
        DatasetModel datasetModel = instantiateModel(entity);
        MetadataModelAssembler metadataModelAssembler = new MetadataModelAssembler();


        if (isNull(entity))
            return datasetModel;

        datasetModel.add(linkTo(
                methodOn(DatasetController.class)

                        .getDatasetById(entity.getDatasetUUID()))
                .withSelfRel());


        datasetModel.setDatasetUUID(entity.getDatasetUUID());
        datasetModel.setDescription(entity.getDescription());
        datasetModel.setName(entity.getName());
        datasetModel.setCreatedAt(entity.getCreatedAt());
        datasetModel.setUpdatedAt(entity.getUpdatedAt());
        datasetModel.setStorageLocation(entity.getStorageLocation());
        datasetModel.setMetadata(metadataModelAssembler.toModel(entity.getMetadata()));
        datasetModel.setVisibility(entity.getVisibility() != null ? DatasetVisibility.valueOf(entity.getVisibility()):null);
        datasetModel.setLinked(entity.getLinked());
        datasetModel.setCatalogLocation(entity.getCatalogLocation());
        datasetModel.setCatalogAuthType(entity.getCatalogAuthType());
        datasetModel.setDataCatalogId(entity.getDataCatalogId());


        return datasetModel;
    }


    @Override
    public CollectionModel<DatasetModel> toCollectionModel(Iterable<? extends DatasetEntity> entities)
    {
        CollectionModel<DatasetModel> datasetModels = super.toCollectionModel(entities);

        datasetModels.add(linkTo(methodOn(DatasetController.class).getAllDatasets()).withSelfRel());

        return datasetModels;
    }

}

