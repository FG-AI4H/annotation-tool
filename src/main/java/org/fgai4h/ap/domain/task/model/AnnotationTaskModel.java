package org.fgai4h.ap.domain.task.model;

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
@JsonRootName(value = "annotationTask")
@Relation(collectionRelation = "annotationTask")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnnotationTaskModel extends RepresentationModel<AnnotationTaskModel> {

    private UUID annotationTaskUUID;

    private String kind;

    private String title;
    private String description;
}
