package org.fgai4h.ap.domain.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.fgai4h.ap.domain.user.AnnotatorModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "annotation")
@Relation(collectionRelation = "annotation")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnnotationModel extends RepresentationModel<AnnotationModel>{

    private UUID annotationUUID;
    private AnnotationTaskModel annotationTask;
    private AnnotationStatus status;
    private String[] data;
    private AnnotatorModel annotator;
    private LocalDateTime submittedAt;
    private TaskModel task;

}
