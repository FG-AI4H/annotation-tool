package org.fgai4h.ap.domain.dataset.controller;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.domain.dataset.mapper.DatasetModelAssembler;
import org.fgai4h.ap.domain.dataset.mapper.MetadataModelAssembler;
import org.fgai4h.ap.domain.dataset.entity.DatasetEntity;
import org.fgai4h.ap.domain.dataset.entity.DatasetRoleEntity;
import org.fgai4h.ap.domain.dataset.model.DatasetModel;
import org.fgai4h.ap.domain.dataset.model.DatasetMetadataModel;
import org.fgai4h.ap.domain.dataset.repository.DatasetRepository;
import org.fgai4h.ap.domain.dataset.repository.DatasetRoleRepository;
import org.fgai4h.ap.domain.dataset.repository.MetadataRepository;
import org.fgai4h.ap.domain.user.entity.UserEntity;
import org.fgai4h.ap.domain.user.model.UserRole;
import org.fgai4h.ap.domain.user.repository.UserRepository;
import org.fgai4h.ap.security.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class DatasetController {

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    private final DatasetRepository datasetRepository;
    private final MetadataRepository metadataRepository;
    private final DatasetRoleRepository datasetRoleRepository;
    private final UserRepository userRepository;
    private final DatasetModelAssembler datasetModelAssembler;
    private final MetadataModelAssembler metadataModelAssembler;

    @GetMapping("/api/v1/datasets")
    public ResponseEntity<CollectionModel<DatasetModel>> getAllDatasets()
    {
        //Move to service
        Authentication authentication = authenticationFacade.getAuthentication();
        List<DatasetEntity> datasetEntities = datasetRepository.findAllByUserId(authentication.getName());
        return new ResponseEntity<>(
                datasetModelAssembler.toCollectionModel(datasetEntities),
                HttpStatus.OK);
    }

    @GetMapping("/api/v1/datasets/{id}")
    public ResponseEntity<DatasetModel> getDatasetById(@PathVariable("id") UUID id)
    {
        return datasetRepository.findById(id)
                .map(datasetModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/api/v1/datasets/{id}")
    public ResponseEntity<String> updateDataset(@RequestBody DatasetEntity dataset, @PathVariable UUID id){
        dataset.setDatasetUUID(id);
        datasetRepository.save(dataset);

        Link newlyCreatedLink = linkTo(methodOn(DatasetController.class).getDatasetById(id)).withSelfRel();

        try {
            return ResponseEntity.noContent().location(new URI(newlyCreatedLink.getHref())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body("Unable to update " + dataset);
        }
    }

    @PostMapping("/api/v1/datasets")
    public ResponseEntity<String> addDataset(@RequestBody DatasetEntity newDataset){

        newDataset = datasetRepository.save(newDataset);

        Authentication authentication = authenticationFacade.getAuthentication();
        UserEntity currentUser = userRepository.findByIdpId(authentication.getName());
        DatasetRoleEntity datasetRole = new DatasetRoleEntity();
        datasetRole.setDataset(newDataset);
        datasetRole.setUserRole(UserRole.OWNER.toString());
        datasetRole.setUser(currentUser);

        datasetRoleRepository.save(datasetRole);

        Link newlyCreatedLink = linkTo(methodOn(DatasetController.class).getDatasetById(newDataset.getDatasetUUID())).withSelfRel();

        try {
            return ResponseEntity.noContent().location(new URI(newlyCreatedLink.getHref())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body("Unable to add " + newDataset);
        }
    }

    @GetMapping("/api/v1/metadata/{id}")
    public ResponseEntity<DatasetMetadataModel> getMetadataById(@PathVariable("id") UUID id)
    {
        return metadataRepository.findById(id)
                .map(metadataModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
