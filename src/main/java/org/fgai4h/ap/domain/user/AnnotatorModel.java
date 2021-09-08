package org.fgai4h.ap.domain.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.springframework.hateoas.server.core.Relation;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "annotator")
@Relation(collectionRelation = "annotator")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnnotatorModel extends UserModel {

    private String expertise;
}
