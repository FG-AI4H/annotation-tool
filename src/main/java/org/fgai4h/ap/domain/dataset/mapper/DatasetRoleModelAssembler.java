package org.fgai4h.ap.domain.dataset.mapper;

import org.fgai4h.ap.domain.dataset.controller.DatasetController;
import org.fgai4h.ap.domain.dataset.controller.DatasetRoleController;
import org.fgai4h.ap.domain.dataset.entity.DatasetRoleEntity;
import org.fgai4h.ap.domain.dataset.model.DatasetRoleModel;
import org.fgai4h.ap.domain.user.mapper.UserModelAssembler;
import org.fgai4h.ap.domain.user.model.UserRole;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DatasetRoleModelAssembler extends RepresentationModelAssemblerSupport<DatasetRoleEntity, DatasetRoleModel> {

    public DatasetRoleModelAssembler() {
        super(DatasetController.class, DatasetRoleModel.class);
    }

    @Override
    public DatasetRoleModel toModel(DatasetRoleEntity entity)
    {
        DatasetRoleModel datasetRoleModel = instantiateModel(entity);
        DatasetModelAssembler datasetModelAssembler = new DatasetModelAssembler();
        UserModelAssembler userModelAssembler = new UserModelAssembler();


        if (isNull(entity))
            return datasetRoleModel;

        datasetRoleModel.add(WebMvcLinkBuilder.linkTo(
                methodOn(DatasetRoleController.class)

                        .getDatasetRoleById(entity.getDatasetRoleUUID()))
                .withSelfRel());


        datasetRoleModel.setDatasetRoleUUID(entity.getDatasetRoleUUID());
        datasetRoleModel.setCreatedAt(entity.getCreatedAt());
        datasetRoleModel.setUpdatedAt(entity.getUpdatedAt());
        datasetRoleModel.setDataset(datasetModelAssembler.toModel(entity.getDataset()));
        datasetRoleModel.setUser(userModelAssembler.toModel(entity.getUser()));
        datasetRoleModel.setUserRole(UserRole.valueOf(entity.getUserRole()));


        return datasetRoleModel;
    }


    @Override
    public CollectionModel<DatasetRoleModel> toCollectionModel(Iterable<? extends DatasetRoleEntity> entities)
    {
        CollectionModel<DatasetRoleModel> datasetModels = super.toCollectionModel(entities);

        datasetModels.add(linkTo(methodOn(DatasetController.class).getAllDatasets()).withSelfRel());

        return datasetModels;
    }

}

