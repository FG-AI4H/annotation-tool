package org.fgai4h.ap.domain.dataset.controller;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.domain.dataset.entity.DatasetRoleEntity;
import org.fgai4h.ap.domain.dataset.mapper.DatasetRoleModelAssembler;
import org.fgai4h.ap.domain.dataset.model.DatasetRoleModel;
import org.fgai4h.ap.domain.dataset.repository.DatasetRoleRepository;
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
public class DatasetRoleController {

    private final DatasetRoleRepository datasetRoleRepository;
    private final DatasetRoleModelAssembler datasetRoleModelAssembler;

    @GetMapping("/api/v1/datasets/{id}/roles")
    public ResponseEntity<CollectionModel<DatasetRoleModel>> getAllDatasetRolesByDatasetId(@PathVariable("id") UUID id)
    {
        List<DatasetRoleEntity> datasetRoleEntities = datasetRoleRepository.findRolesByDatasetId(id);
        return new ResponseEntity<>(
                datasetRoleModelAssembler.toCollectionModel(datasetRoleEntities),
                HttpStatus.OK);
    }

    @GetMapping("/api/v1/dataset_roles/{id}")
    public ResponseEntity<DatasetRoleModel> getDatasetRoleById(@PathVariable("id") UUID id)
    {
        return datasetRoleRepository.findById(id)
                .map(datasetRoleModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/api/v1/dataset_roles/{id}")
    public ResponseEntity<?> updateDataset(@RequestBody DatasetRoleEntity datasetRole, @PathVariable UUID id){
        DatasetRoleEntity datasetRoleToUpdate = datasetRole;
        datasetRoleToUpdate.setDatasetRoleUUID(id);
        datasetRoleRepository.save(datasetRoleToUpdate);

        Link newlyCreatedLink = linkTo(methodOn(DatasetRoleController.class).getDatasetRoleById(id)).withSelfRel();

        try {
            return ResponseEntity.noContent().location(new URI(newlyCreatedLink.getHref())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body("Unable to update " + datasetRoleToUpdate);
        }
    }

    @PostMapping("/api/v1/dataset_roles")
    public ResponseEntity<?> addDataset(@RequestBody DatasetRoleEntity newDatasetRole){

        newDatasetRole = datasetRoleRepository.save(newDatasetRole);

        Link newlyCreatedLink = linkTo(methodOn(DatasetController.class).getDatasetById(newDatasetRole.getDatasetRoleUUID())).withSelfRel();

        try {
            return ResponseEntity.noContent().location(new URI(newlyCreatedLink.getHref())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body("Unable to add " + newDatasetRole);
        }
    }


}
