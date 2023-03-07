package org.fgai4h.ap.domain.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fgai4h.ap.domain.user.entity.UserEntity;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="annoatation")
public class AnnotationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID annotationUUID;

    @OneToOne
    private AnnotationTaskEntity annotationTask;

    private AnnotationStatus status;

    @OneToMany(mappedBy = "annotationEntity", cascade=CascadeType.ALL)
    private List<AnnotationDataEntity> annotationDataList;

    @OneToOne
    private UserEntity annotator;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime submittedAt;

    @ManyToOne
    private TaskEntity task;

}
