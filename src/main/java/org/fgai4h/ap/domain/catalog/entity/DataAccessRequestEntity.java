package org.fgai4h.ap.domain.catalog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fgai4h.ap.domain.dataset.entity.DatasetEntity;
import org.fgai4h.ap.domain.user.entity.UserEntity;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="dataAccessRequest")
public class DataAccessRequestEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID dataAccessRequestUUID;

    private LocalDateTime requestDate;
    private String requestStatus;
    private LocalDateTime requestStatusDate;
    private String motivation;

    @OneToOne
    private UserEntity requester;

    @OneToOne
    private UserEntity dataOwner;

    @OneToOne
    private DatasetEntity dataset;
}
