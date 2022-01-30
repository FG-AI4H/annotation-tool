package org.fgai4h.ap.domain.task;

import org.fgai4h.ap.domain.user.UserModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AnnotationModelAssembler extends RepresentationModelAssemblerSupport<AnnotationEntity, AnnotationModel> {

    public AnnotationModelAssembler() {
        super(TaskController.class, AnnotationModel.class);
    }

    @Override
    public AnnotationModel toModel(AnnotationEntity entity) {
        AnnotationTaskModelAssembler annotationTaskModelAssembler = new AnnotationTaskModelAssembler();
        UserModelAssembler userModelAssembler = new UserModelAssembler();

        AnnotationModel annotationModel = instantiateModel(entity);

        annotationModel.add(linkTo(
                methodOn(TaskController.class)
                        .getAnnotationById(entity.getAnnotationUUID()))
                .withSelfRel());

        annotationModel.setAnnotationUUID(entity.getAnnotationUUID());
        annotationModel.setAnnotationTask(annotationTaskModelAssembler.toModel(entity.getAnnotationTask()));
        annotationModel.setAnnotator(userModelAssembler.toModel(entity.getAnnotator()));
        annotationModel.setData(toAnnotationDataModel(entity.getAnnotationDataList()));
        annotationModel.setStatus(entity.getStatus());
        annotationModel.setSubmittedAt(entity.getSubmittedAt());
        return annotationModel;
    }

    @Override
    public CollectionModel<AnnotationModel> toCollectionModel(Iterable<? extends AnnotationEntity> entities)
    {
        CollectionModel<AnnotationModel> annotationModels = super.toCollectionModel(entities);

        annotationModels.add(linkTo(methodOn(TaskController.class).getAllAnnotations()).withSelfRel());

        return annotationModels;
    }

    public List<AnnotationModel> toAnnotationModel(List<AnnotationEntity> annotations) {
        if (annotations.isEmpty())
            return Collections.emptyList();

        UserModelAssembler userModelAssembler = new UserModelAssembler();
        AnnotationTaskModelAssembler annotationTaskModelAssembler = new AnnotationTaskModelAssembler();

        return annotations.stream()
                .map(annotation -> AnnotationModel.builder()
                        .annotationUUID(annotation.getAnnotationUUID())
                        .data(toAnnotationDataModel(annotation.getAnnotationDataList()))
                        .status(annotation.getStatus())
                        .submittedAt(annotation.getSubmittedAt())
                        .annotationTask(annotationTaskModelAssembler.toModel(annotation.getAnnotationTask()))
                        .annotator(userModelAssembler.toModel(annotation.getAnnotator()))
                        .build()
                        .add(linkTo(
                                methodOn(TaskController.class)
                                        .getAnnotationById(annotation.getAnnotationUUID()))
                                .withSelfRel()))
                .collect(Collectors.toList());
    }

    private List<AnnotationDataModel> toAnnotationDataModel(List<AnnotationDataEntity> annotations) {
        if (annotations.isEmpty())
            return Collections.emptyList();

        return annotations.stream()
                .map(annotation -> AnnotationDataModel.builder()
                        .annotationDataUUID(annotation.getAnnotationDataUUID())
                        .data(annotation.getData())
                        .build())
                .collect(Collectors.toList());
    }

}
