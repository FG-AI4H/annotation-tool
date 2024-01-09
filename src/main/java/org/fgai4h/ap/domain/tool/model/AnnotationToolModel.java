package org.fgai4h.ap.domain.tool.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.fgai4h.ap.domain.task.model.AnnotationTaskModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "annotationTool")
@Relation(collectionRelation = "annotationTool")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnnotationToolModel extends RepresentationModel<AnnotationToolModel> {

    private UUID annotationToolUUID;
    private String name;
    private String description;
    private String editor;
    private List<AnnotationTaskModel> annotationTasks;
}
