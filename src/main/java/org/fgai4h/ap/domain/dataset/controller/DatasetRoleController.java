package org.fgai4h.ap.domain.dataset.controller;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.api.DatasetRoleApi;
import org.fgai4h.ap.api.model.DatasetRoleDto;
import org.fgai4h.ap.domain.dataset.entity.DatasetRoleEntity;
import org.fgai4h.ap.domain.dataset.mapper.DatasetRoleApiMapper;
import org.fgai4h.ap.domain.dataset.mapper.DatasetRoleModelAssembler;
import org.fgai4h.ap.domain.dataset.model.DatasetModel;
import org.fgai4h.ap.domain.dataset.model.DatasetRoleModel;
import org.fgai4h.ap.domain.dataset.repository.DatasetRoleRepository;
import org.fgai4h.ap.domain.dataset.service.DatasetRoleService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class DatasetRoleController implements DatasetRoleApi {

    private final DatasetRoleApiMapper datasetRoleApiMapper;
    private final DatasetRoleService datasetRoleService;

    @Override
    public ResponseEntity<DatasetRoleDto> getDatasetRoleById(UUID id)
    {
        return datasetRoleService.getDatasetRoleById(id)
                .map(datasetRoleApiMapper::toDatasetRoleDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> updateDatasetRole(UUID id, DatasetRoleDto datasetRoleDto){
        DatasetRoleModel datasetRoleModel = datasetRoleApiMapper.toDatasetRoleModel(datasetRoleDto);
        datasetRoleService.updateDatasetRole(datasetRoleModel);

        Link newlyCreatedLink = linkTo(methodOn(DatasetRoleController.class).getDatasetRoleById(id)).withSelfRel();

        try {
            return ResponseEntity.noContent().location(new URI(newlyCreatedLink.getHref())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<Void> addDatasetRole(DatasetRoleDto datasetRoleDto){

        DatasetRoleModel datasetRoleModel = datasetRoleApiMapper.toDatasetRoleModel(datasetRoleDto);
        datasetRoleModel = datasetRoleService.addDatasetRole(datasetRoleModel);

        Link newlyCreatedLink = linkTo(methodOn(DatasetController.class).getDatasetById(datasetRoleModel.getDatasetRoleUUID())).withSelfRel();

        try {
            return ResponseEntity.noContent().location(new URI(newlyCreatedLink.getHref())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteDatasetRoleById(UUID datasetRoleId) {
        datasetRoleService.deleteDatasetRoleById(datasetRoleId);
        return ResponseEntity.noContent().build();
    }


}
