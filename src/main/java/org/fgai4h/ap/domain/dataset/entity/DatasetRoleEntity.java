package org.fgai4h.ap.domain.dataset.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fgai4h.ap.domain.user.entity.UserEntity;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="datasetRole")
public class DatasetRoleEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID datasetRoleUUID;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToOne(cascade = {CascadeType.ALL})
    private DatasetEntity dataset;

    @OneToOne(cascade = {CascadeType.ALL})
    private UserEntity user;

    private String userRole;
    private String visibility;

}
