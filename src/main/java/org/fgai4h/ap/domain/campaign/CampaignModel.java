package org.fgai4h.ap.domain.campaign;

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
@JsonRootName(value = "campaign")
@Relation(collectionRelation = "campaign")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CampaignModel extends RepresentationModel<CampaignModel> {

    private UUID campaignUUID;
    private String name;
    private String description;
    private String status;
}
