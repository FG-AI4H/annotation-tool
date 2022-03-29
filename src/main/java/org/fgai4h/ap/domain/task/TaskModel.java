package org.fgai4h.ap.domain.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.fgai4h.ap.domain.campaign.CampaignModel;
import org.fgai4h.ap.domain.user.UserModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonRootName(value = "task")
@Relation(collectionRelation = "task")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskModel extends RepresentationModel<TaskModel> {

    private UUID taskUUID;
    private String kind;
    private Boolean readOnly;
    private List<AnnotationTaskModel> annotationTasks;
    private List<SampleModel> samples;
    private List<AnnotationModel> annotations;
    private UserModel assignee;
    private CampaignModel campaign;


}
