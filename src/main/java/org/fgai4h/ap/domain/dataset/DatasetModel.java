package org.fgai4h.ap.domain.dataset;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "dataset")
@Relation(collectionRelation = "dataset")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DatasetModel extends RepresentationModel<DatasetModel> {

    private UUID datasetUUID;
    private String name;
    private String description;
    private String storageLocation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private MetadataModel metadata;
}
