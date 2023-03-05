package org.fgai4h.ap.domain.dataset;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.fgai4h.ap.domain.user.UserModel;
import org.fgai4h.ap.domain.user.UserRole;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "datasetRole")
@Relation(collectionRelation = "datasetRole")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DatasetRoleModel extends RepresentationModel<DatasetRoleModel> {

    private UUID datasetRoleUUID;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private DatasetModel dataset;
    private UserModel user;
    private UserRole userRole;
}
