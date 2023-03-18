package org.fgai4h.ap.domain.task.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.fgai4h.ap.domain.user.model.UserModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.List;
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
    private List<AnnotationDataModel> data;
    private UserModel annotator;
    private LocalDateTime submittedAt;

}
