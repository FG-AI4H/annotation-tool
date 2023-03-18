package org.fgai4h.ap.domain.task.mapper;

import org.fgai4h.ap.api.model.TaskDto;
import org.fgai4h.ap.domain.campaign.mapper.CampaignApiMapper;
import org.fgai4h.ap.domain.campaign.service.CampaignService;
import org.fgai4h.ap.domain.task.model.*;
import org.fgai4h.ap.domain.task.service.TaskService;
import org.fgai4h.ap.domain.user.mapper.AnnotatorApiMapper;
import org.fgai4h.ap.domain.user.mapper.UserApiMapper;
import org.fgai4h.ap.domain.user.service.UserService;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = {UserService.class, TaskService.class, CampaignService.class, UserApiMapper.class, CampaignApiMapper.class, AnnotationTaskApiMapper.class, AnnotatorApiMapper.class, SampleApiMapper.class, CampaignApiMapper.class})
public interface TaskApiMapper {

    @Mapping(source = "id", target = "taskUUID")
    TaskModel toTaskModel(TaskDto taskDto);

    List<AnnotationTaskModel> toAnnotationTaskModelList(List<UUID> value);
    List<SampleModel> toSampleModelList(List<UUID> value);
    List<AnnotationModel> toAnnotationModelList(List<UUID> value);

    List<UUID> toSampleUUIDList(List<SampleModel> value);
    List<UUID> toAnnotationUUIDList(List<AnnotationModel> value);

    @InheritInverseConfiguration
    @Mapping(source = "campaign.status", target="campaignStatus")
    @Mapping(source = "campaign.annotationKind", target="campaignTaskKind")
    @Mapping(source = "assignee.username", target="assigneeUsername")
    TaskDto toTaskDto(final TaskModel taskModel);

}
