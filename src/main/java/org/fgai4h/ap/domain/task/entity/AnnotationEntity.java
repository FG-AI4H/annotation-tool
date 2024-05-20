package org.fgai4h.ap.domain.task.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fgai4h.ap.domain.task.model.AnnotationStatus;
import org.fgai4h.ap.domain.user.entity.UserEntity;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name= "annotation")
public class AnnotationEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    
    private UUID annotationUUID;

    @OneToOne
    private AnnotationTaskEntity annotationTask;

    private AnnotationStatus status;

    @OneToMany(mappedBy = "annotationEntity", cascade=CascadeType.ALL)
    private List<AnnotationDataEntity> annotationDataList;

    @OneToOne
    private UserEntity annotator;

    private LocalDateTime submittedAt;

    @ManyToOne
    private TaskEntity task;

}
