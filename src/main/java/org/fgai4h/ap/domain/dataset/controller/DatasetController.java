package org.fgai4h.ap.domain.dataset.controller;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.api.DatasetApi;
import org.fgai4h.ap.api.model.DatasetDto;
import org.fgai4h.ap.domain.dataset.entity.DatasetEntity;
import org.fgai4h.ap.domain.dataset.mapper.DatasetApiMapper;
import org.fgai4h.ap.domain.dataset.mapper.DatasetModelAssembler;
import org.fgai4h.ap.domain.dataset.mapper.MetadataModelAssembler;
import org.fgai4h.ap.domain.dataset.model.DatasetMetadataModel;
import org.fgai4h.ap.domain.dataset.model.DatasetModel;
import org.fgai4h.ap.domain.dataset.repository.DatasetRepository;
import org.fgai4h.ap.domain.dataset.repository.DatasetRoleRepository;
import org.fgai4h.ap.domain.dataset.repository.MetadataRepository;
import org.fgai4h.ap.domain.dataset.service.DatasetService;
import org.fgai4h.ap.domain.user.repository.UserRepository;
import org.fgai4h.ap.security.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class DatasetController implements DatasetApi {

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    private final DatasetRepository datasetRepository;
    private final MetadataRepository metadataRepository;
    private final DatasetRoleRepository datasetRoleRepository;
    private final UserRepository userRepository;
    private final DatasetModelAssembler datasetModelAssembler;
    private final MetadataModelAssembler metadataModelAssembler;
    private final DatasetService datasetService;
    private final DatasetApiMapper datasetApiMapper;

    @Override
    public ResponseEntity<List<DatasetDto>> getAllDatasets() {
        Authentication authentication = authenticationFacade.getAuthentication();
        return new ResponseEntity<>(
                datasetService.getAllDatasets(authentication.getName()).stream().map(datasetApiMapper::toDatasetDto).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DatasetDto> addDataset(DatasetDto datasetDto) {
        Authentication authentication = authenticationFacade.getAuthentication();
        DatasetModel datasetModel = datasetApiMapper.toDatasetModel(datasetDto);
        datasetModel = datasetService.addDataset(datasetModel, authentication.getName());
        return new ResponseEntity<DatasetDto>(datasetApiMapper.toDatasetDto(datasetModel),HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<DatasetDto> getDatasetById(UUID datasetId) {
        return datasetService.getDatasetById(datasetId)
                .map(datasetApiMapper::toDatasetDto)
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

    @GetMapping("/api/v1/metadata/{id}")
    public ResponseEntity<DatasetMetadataModel> getMetadataById(@PathVariable("id") UUID id)
    {
        return metadataRepository.findById(id)
                .map(metadataModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
