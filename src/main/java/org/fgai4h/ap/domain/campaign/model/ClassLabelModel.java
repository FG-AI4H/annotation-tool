package org.fgai4h.ap.domain.campaign.model;


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
@JsonRootName(value = "classLabel")
@Relation(collectionRelation = "classLabel")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassLabelModel extends RepresentationModel<ClassLabelModel> {

    private UUID classLabelUUID;
    private String name;
    private String description;

}
