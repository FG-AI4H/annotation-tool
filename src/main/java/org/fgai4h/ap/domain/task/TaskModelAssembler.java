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

    public TaskModelAssembler() {
        super(TaskController.class, TaskModel.class);
    }

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

    private List<AnnotationModel> toAnnotationModel(List<AnnotationEntity> annotations) {
        if (annotations.isEmpty())
            return Collections.emptyList();

        return annotations.stream()
                .map(annotation -> AnnotationModel.builder()
                        .annotationUUID(annotation.getAnnotationUUID())
                        .data(annotation.getData())
                        .build()
                        .add(linkTo(
                                methodOn(TaskController.class)
                                        .getAnnotationById(annotation.getAnnotationUUID()))
                                .withSelfRel()))
                .collect(Collectors.toList());
    }

    private List<SampleModel> toSampleModel(List<SampleEntity> samples) {
        if (samples.isEmpty())
            return Collections.emptyList();

        return samples.stream()
                .map(sample -> SampleModel.builder()
                .sampleUUID(sample.getSampleUUID())
                        .data(sample.getData())
                        .title(sample.getTitle())
                        .task(toModel(sample.getTask()))
                        .build()
                        .add(linkTo(methodOn(TaskController.class).getSampleById(sample.getSampleUUID()))
                        .withSelfRel())
                ).collect(Collectors.toList());

    }

    private List<AnnotationTaskModel> toAnnotationTaskModel(List<AnnotationTaskEntity> annotationTasks) {
        if (annotationTasks.isEmpty())
            return Collections.emptyList();

        return annotationTasks.stream()
                .map(annotationTask -> AnnotationTaskModel.builder()
                        .annotationTaskUUID(annotationTask.getAnnotationTaskUUID())
                        .title(annotationTask.getTitle())
                        .kind(annotationTask.getKind())
                        .description(annotationTask.getDescription())
                        .build()
                        .add(linkTo(methodOn(TaskController.class).getAnnotationTaskById(annotationTask.getAnnotationTaskUUID()))
                                .withSelfRel())
                ).collect(Collectors.toList());
    }

    @Override
    public CollectionModel<TaskModel> toCollectionModel(Iterable<? extends TaskEntity> entities){
        CollectionModel<TaskModel> taskModels = super.toCollectionModel(entities);

        //taskModels.add(linkTo(methodOn(AnnotationController.class).getAllAnnotation()).withSelfRel());

        return taskModels;
    }
}
