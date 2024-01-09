package org.fgai4h.ap.domain.dataset.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="dataset")
public class DatasetEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID datasetUUID;

    private String name;
    private String description;
    private String storageLocation;
    private Boolean linked;
    private String catalogLocation;
    private String catalogAuthType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID dataCatalogId;

    private String visibility;

    @OneToOne(cascade = {CascadeType.ALL})
    private DatasetMetadataEntity metadata;
}
