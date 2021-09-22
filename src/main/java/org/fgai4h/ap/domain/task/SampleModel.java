package org.fgai4h.ap.domain.task;

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
@JsonRootName(value = "sample")
@Relation(collectionRelation = "sample")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SampleModel extends RepresentationModel<SampleModel> {

    private UUID sampleUUID;
    private String title;
    private String data;
}
