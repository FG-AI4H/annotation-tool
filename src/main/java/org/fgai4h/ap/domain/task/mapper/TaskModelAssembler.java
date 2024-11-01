package org.fgai4h.ap.domain.task.mapper;

import org.fgai4h.ap.api.model.TaskStatus;
import org.fgai4h.ap.domain.campaign.mapper.CampaignModelAssembler;
import org.fgai4h.ap.domain.task.controller.TaskController;
import org.fgai4h.ap.domain.task.entity.SampleEntity;
import org.fgai4h.ap.domain.task.entity.TaskEntity;
import org.fgai4h.ap.domain.task.model.AnnotationStatus;
import org.fgai4h.ap.domain.task.model.SampleModel;
import org.fgai4h.ap.domain.task.model.TaskKind;
import org.fgai4h.ap.domain.task.model.TaskModel;
import org.fgai4h.ap.domain.user.mapper.UserModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TaskModelAssembler extends RepresentationModelAssemblerSupport<TaskEntity, TaskModel> {

    public TaskModelAssembler() {
        super(TaskController.class, TaskModel.class);
    }

    @Override
    public TaskModel toModel(TaskEntity entity) {
        TaskModel taskModel = instantiateModel(entity);

        AnnotationModelAssembler annotationModelAssembler = new AnnotationModelAssembler();
        AnnotationTaskModelAssembler annotationTaskModelAssembler = new AnnotationTaskModelAssembler();
        UserModelAssembler userModelAssembler = new UserModelAssembler();
        CampaignModelAssembler campaignModelAssembler = new CampaignModelAssembler();

        taskModel.add(linkTo(
                methodOn(TaskController.class)
                        .getTaskById(entity.getTaskUUID()))
                .withSelfRel());

        taskModel.setTaskUUID(entity.getTaskUUID());
        taskModel.setKind(TaskKind.valueOf(entity.getKind()));
        taskModel.setReadOnly(entity.getReadOnly());
        taskModel.setAnnotationTasks(annotationTaskModelAssembler.toAnnotationTaskModel(entity.getAnnotationTasks()));
        taskModel.setSamples(toSampleModel(entity.getSamples()));
        taskModel.setAnnotations(annotationModelAssembler.toAnnotationModel(entity.getAnnotations()));
        taskModel.setAssignee(userModelAssembler.toModel(entity.getAssignee()));
        taskModel.setCampaign(campaignModelAssembler.toModel(entity.getCampaign()));

        taskModel.setTaskStatus(TaskStatus.INITIALIZED);
        //Check if all annotation tasks are completed
        if (entity.getAnnotations().stream().allMatch(annotation -> annotation.getStatus().equals(AnnotationStatus.COMPLETED))) {
            taskModel.setTaskStatus(TaskStatus.COMPLETED);
        }
        return taskModel;
    }



    private List<SampleModel> toSampleModel(List<SampleEntity> samples) {
        if (samples.isEmpty())
            return Collections.emptyList();

        return samples.stream()
                .map(sample -> SampleModel.builder()
                .sampleUUID(sample.getSampleUUID())
                        .data(sample.getData())
                        .title(sample.getTitle())
                        .build()
                        .add(linkTo(methodOn(TaskController.class).getSampleById(sample.getSampleUUID()))
                        .withSelfRel())
                ).collect(Collectors.toList());

    }

    @Override
    public CollectionModel<TaskModel> toCollectionModel(Iterable<? extends TaskEntity> entities){
        CollectionModel<TaskModel> taskModels = super.toCollectionModel(entities);

        taskModels.add(linkTo(methodOn(TaskController.class).getAllTasks()).withSelfRel());

        return taskModels;
    }
}
