package org.fgai4h.ap.domain.user;

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
@JsonRootName(value = "annotator")
@Relation(collectionRelation = "annotator")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnnotatorModel extends RepresentationModel<AnnotatorModel> {

    private UUID annotatorUUID;

    private String expertise;
    private Integer yearsInPractice;
    private Float expectedSalary;
    private String workCountry;
    private String studyCountry;
    private Integer selfAssessment;
    private String degree;
}
