package org.fgai4h.ap.domain.task;

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

    @Override
    public TaskModel toModel(TaskEntity entity) {
        TaskModel taskModel = instantiateModel(entity);

        taskModel.add(linkTo(
                methodOn(TaskController.class)
                        .getTaskById(entity.getTaskUUID()))
                .withSelfRel());

        taskModel.setTaskUUID(entity.getTaskUUID());
        taskModel.setKind(entity.getKind());
        taskModel.setReadOnly(entity.getReadOnly());
        taskModel.setAnnotationTasks(toAnnotationTaskModel(entity.getAnnotationTasks()));
        taskModel.setSamples(toSampleModel(entity.getSamples()));
        taskModel.setAnnotations(toAnnotationModel(entity.getAnnotations()));
        return taskModel;
    }

    private List<AnnotationEntity> toAnnotationModel(List<AnnotationEntity> annotations) {
        if (annotations.isEmpty())
            return Collections.emptyList();

        return annotations.stream()
                .map(annotation -> AnnotationModel.builder()
                        .annotationUUID(annotation.getAnnotationUUID())
                        .title(annotation.getTitle())
                        .build()
                        .add(linkTo(
                                methodOn(TaskController.class)
                                        .getAnnotationById(annotation.getId()))
                                .withSelfRel()))
                .collect(Collectors.toList());
    }

    private List<SampleEntity> toSampleModel(List<SampleEntity> samples) {
        return null;
    }

    private List<AnnotationTaskEntity> toAnnotationTaskModel(List<AnnotationTaskEntity> annotationTasks) {
        return null;
    }

    @Override
    public CollectionModel<TaskModel> toCollectionModel(Iterable<? extends TaskEntity> entities){

    }
}
