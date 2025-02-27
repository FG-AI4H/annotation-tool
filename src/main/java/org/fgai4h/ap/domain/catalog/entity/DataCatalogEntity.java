package org.fgai4h.ap.domain.catalog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="dataCatalog")
public class DataCatalogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    
    private UUID dataCatalogUUID;

    private String name;
    private String description;
    private String provider;
    private String providerCatalogId;
    private String location;
    private String databaseName;
    private String awsRegion;
    private String bucketName;
}
