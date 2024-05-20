package org.fgai4h.ap.domain.task.mapper;

import org.fgai4h.ap.domain.task.controller.TaskController;
import org.fgai4h.ap.domain.task.entity.AnnotationTaskEntity;
import org.fgai4h.ap.domain.task.model.AnnotationTaskModel;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AnnotationTaskModelAssembler extends RepresentationModelAssemblerSupport<AnnotationTaskEntity, AnnotationTaskModel> {

    public AnnotationTaskModelAssembler() {
        super(TaskController.class, AnnotationTaskModel.class);
    }


    @Override
    public @NotNull AnnotationTaskModel toModel(@NotNull AnnotationTaskEntity entity) {

        AnnotationTaskModel annotationTaskModel = instantiateModel(entity);

        annotationTaskModel.add(linkTo(
                methodOn(TaskController.class)
                        .getAnnotationTaskById(entity.getAnnotationTaskUUID()))
                .withSelfRel());

        annotationTaskModel.setAnnotationTaskUUID(entity.getAnnotationTaskUUID());
        annotationTaskModel.setDescription(entity.getDescription());
        annotationTaskModel.setKind(entity.getKind());
        annotationTaskModel.setTitle(entity.getTitle());

        return annotationTaskModel;
    }

    public List<AnnotationTaskModel> toAnnotationTaskModel(List<AnnotationTaskEntity> annotationTasks) {
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
}
