package org.fgai4h.ap.domain.catalog.controller;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.api.CatalogApi;
import org.fgai4h.ap.api.model.DataCatalogDto;
import org.fgai4h.ap.api.model.TableDto;
import org.fgai4h.ap.domain.catalog.mapper.DataCatalogApiMapper;
import org.fgai4h.ap.domain.catalog.model.DataCatalogModel;
import org.fgai4h.ap.domain.catalog.model.TableModel;
import org.fgai4h.ap.domain.catalog.service.DataCatalogService;
import org.fgai4h.ap.helpers.AWSGlue;
import org.fgai4h.ap.security.AuthenticationFacade;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class DataCatalogController implements CatalogApi {

    private final DataCatalogService dataCatalogService;
    private final DataCatalogApiMapper dataCatalogApiMapper;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public ResponseEntity<List<DataCatalogDto>> getDataCatalogs() {
        Authentication authentication = authenticationFacade.getAuthentication();
        return new ResponseEntity<>(
                dataCatalogService.getDataCatalogs(authentication.getName()).stream().map(dataCatalogApiMapper::toDataCatalogDto).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DataCatalogDto> getDataCatalogById(UUID dataCatalogId) {
        return dataCatalogService.getDataCatalogById(dataCatalogId)
                .map(dataCatalogApiMapper::toDataCatalogDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> addDataCatalog(DataCatalogDto dataCatalogDto) {
        DataCatalogModel dataCatalogModel = dataCatalogApiMapper.toDataCatalogModel(dataCatalogDto);
        dataCatalogModel = dataCatalogService.addDataCatalog(dataCatalogModel);

        try {
            return ResponseEntity.created(new URI(dataCatalogModel.getDataCatalogUUID().toString())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<Void> updateDataCatalog(UUID dataCatalogId, DataCatalogDto dataCatalogDto) {
        DataCatalogModel dataCatalogModel = dataCatalogApiMapper.toDataCatalogModel(dataCatalogDto);
        dataCatalogService.updateDataCatalog(dataCatalogModel);

        Link newlyCreatedLink = linkTo(methodOn(DataCatalogController.class).getDataCatalogById(dataCatalogId)).withSelfRel();

        try {
            return ResponseEntity.noContent().location(new URI(newlyCreatedLink.getHref())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteDataCatalogById(UUID dataCatalogId) {
        dataCatalogService.deleteDataCatalogById(dataCatalogId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<TableDto> getTableFromDataCatalog(UUID dataCatalogId, String tableName) {
        Optional<DataCatalogModel> dataCatalog = dataCatalogService.getDataCatalogById(dataCatalogId);
        Optional<TableModel> tableModel = Optional.empty();
        if(dataCatalog.isPresent()) {
            tableModel = AWSGlue.getGlueTable(dataCatalog.get() ,tableName);
        }
        
        return tableModel.map(dataCatalogApiMapper::toTableDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

