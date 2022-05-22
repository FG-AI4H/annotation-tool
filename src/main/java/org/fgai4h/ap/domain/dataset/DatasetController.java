package org.fgai4h.ap.domain.dataset;

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
public class DatasetController {

    private final DatasetRepository datasetRepository;
    private final DatasetModelAssembler datasetModelAssembler;

    public DatasetController(DatasetRepository datasetRepository, DatasetModelAssembler datasetModelAssembler) {
        this.datasetRepository = datasetRepository;
        this.datasetModelAssembler = datasetModelAssembler;
    }


    @GetMapping("/api/v1/datasets")
    public ResponseEntity<CollectionModel<DatasetModel>> getAllDatasets()
    {
        List<DatasetEntity> datasetEntities = datasetRepository.findAll();
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
    public ResponseEntity<?> updateDataset(@RequestBody DatasetEntity dataset, @PathVariable UUID id){
        DatasetEntity datasetToUpdate = dataset;
        datasetToUpdate.setDatasetUUID(id);
        datasetRepository.save(datasetToUpdate);

        Link newlyCreatedLink = linkTo(methodOn(DatasetController.class).getDatasetById(id)).withSelfRel();

        try {
            return ResponseEntity.noContent().location(new URI(newlyCreatedLink.getHref())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body("Unable to update " + datasetToUpdate);
        }
    }

    @PostMapping("/api/v1/datasets")
    public ResponseEntity<?> addDataset(@RequestBody DatasetEntity newDataset){

        newDataset = datasetRepository.save(newDataset);

        Link newlyCreatedLink = linkTo(methodOn(DatasetController.class).getDatasetById(newDataset.getDatasetUUID())).withSelfRel();

        try {
            return ResponseEntity.noContent().location(new URI(newlyCreatedLink.getHref())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body("Unable to add " + newDataset);
        }
    }
}
