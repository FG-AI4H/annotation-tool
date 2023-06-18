package org.fgai4h.ap.domain.dataset.controller;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.api.DatasetApi;
import org.fgai4h.ap.api.model.DatasetDto;
import org.fgai4h.ap.api.model.DatasetRoleDto;
import org.fgai4h.ap.domain.dataset.mapper.DatasetApiMapper;
import org.fgai4h.ap.domain.dataset.mapper.DatasetRoleApiMapper;
import org.fgai4h.ap.domain.dataset.mapper.MetadataModelAssembler;
import org.fgai4h.ap.domain.dataset.model.DatasetMetadataModel;
import org.fgai4h.ap.domain.dataset.model.DatasetModel;
import org.fgai4h.ap.domain.dataset.repository.MetadataRepository;
import org.fgai4h.ap.domain.dataset.service.DatasetRoleService;
import org.fgai4h.ap.domain.dataset.service.DatasetService;
import org.fgai4h.ap.security.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
    private final MetadataRepository metadataRepository;
    private final MetadataModelAssembler metadataModelAssembler;
    private final DatasetService datasetService;
    private final DatasetApiMapper datasetApiMapper;
    private final DatasetRoleService datasetRoleService;
    private final DatasetRoleApiMapper datasetRoleApiMapper;

    @Override
    public ResponseEntity<List<DatasetDto>> getAllDatasets() {
        Authentication authentication = authenticationFacade.getAuthentication();
        return new ResponseEntity<>(
                datasetService.getAllDatasets(authentication.getName()).stream().map(datasetApiMapper::toDatasetDto).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> addDataset(DatasetDto datasetDto) {
        Authentication authentication = authenticationFacade.getAuthentication();
        DatasetModel datasetModel = datasetApiMapper.toDatasetModel(datasetDto);
        datasetModel = datasetService.addDataset(datasetModel, authentication.getName());

        try {
            return ResponseEntity.created(new URI(datasetModel.getDatasetUUID().toString())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<DatasetDto> getDatasetById(UUID datasetId) {
        return datasetService.getDatasetById(datasetId)
                .map(datasetApiMapper::toDatasetDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> updateDatatset(UUID datasetId, DatasetDto datasetDto) {
        DatasetModel datasetModel = datasetApiMapper.toDatasetModel(datasetDto);
        datasetService.updateDataset(datasetModel);

        Link newlyCreatedLink = linkTo(methodOn(DatasetController.class).getDatasetById(datasetId)).withSelfRel();

        try {
            return ResponseEntity.noContent().location(new URI(newlyCreatedLink.getHref())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteDatasetById(UUID datasetId) {
        datasetService.deleteCampaignById(datasetId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<DatasetRoleDto>> getDatasetPermissionsById(UUID datasetId) {
        return new ResponseEntity<>(datasetRoleService.getDatasetRolesForDatasetId(datasetId).stream().map(datasetRoleApiMapper::toDatasetRoleDto).collect(Collectors.toList()),
        HttpStatus.OK);
    }

    @GetMapping("/api/v1/metadata/{id}")
    public ResponseEntity<DatasetMetadataModel> getMetadataById(@PathVariable("id") UUID id)
    {
        return metadataRepository.findById(id)
                .map(metadataModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<DatasetDto>> getCatalogDatasets() {
        Authentication authentication = authenticationFacade.getAuthentication();
        return new ResponseEntity<>(
                datasetService.getCatalogDatasets(authentication.getName()).stream().map(datasetApiMapper::toDatasetDto).collect(Collectors.toList()),
                HttpStatus.OK);
    }
}
