package org.fgai4h.ap.domain.catalog.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "dataCatalog")
@Relation(collectionRelation = "dataCatalog")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataCatalogModel extends RepresentationModel<DataCatalogModel> {

    private UUID dataCatalogUUID;
    private String name;
    private String description;
    private String provider;
    private String providerCatalogId;
    private String location;
    private String databaseName;
    private String awsRegion;
}
