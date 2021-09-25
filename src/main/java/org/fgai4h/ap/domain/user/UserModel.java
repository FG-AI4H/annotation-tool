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
@JsonRootName(value = "user")
@Relation(collectionRelation = "user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserModel extends RepresentationModel<UserModel> {
    private UUID userUUID;
    private String username;
}
