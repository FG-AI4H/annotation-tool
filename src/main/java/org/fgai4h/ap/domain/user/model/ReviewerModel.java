package org.fgai4h.ap.domain.user.model;

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
@JsonRootName(value = "reviewer")
@Relation(collectionRelation = "reviewer")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewerModel extends RepresentationModel<ReviewerModel> {

    private UUID reviewerUUID;

    private Float expectedSalary;
    private Integer selfAssessment;
}
