package org.fgai4h.ap.domain.catalog.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.fgai4h.ap.domain.dataset.model.DatasetCatalogRequestStatusModel;
import org.fgai4h.ap.domain.dataset.model.DatasetModel;
import org.fgai4h.ap.domain.user.model.UserModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "dataAccessRequest")
@Relation(collectionRelation = "dataAccessRequest")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataAccessRequestModel extends RepresentationModel<DataAccessRequestModel> {

    private UUID dataAccessRequestUUID;
    private LocalDateTime requestDate;
    private DatasetCatalogRequestStatusModel requestStatus;
    private LocalDateTime requestStatusDate;
    private UserModel requester;
    private UserModel dataOwner;
    private DatasetModel dataset;
    private String motivation;

}
