package org.fgai4h.ap.domain.task;

import org.fgai4h.ap.domain.user.AnnotatorModelAssembler;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AnnotationModelAssembler extends RepresentationModelAssemblerSupport<AnnotationEntity, AnnotationModel> {

    public AnnotationModelAssembler() {
        super(TaskController.class, AnnotationModel.class);
    }

    @Override
    public AnnotationModel toModel(AnnotationEntity entity) {
        TaskModelAssembler taskModelAssembler = new TaskModelAssembler();
        AnnotationTaskModelAssembler annotationTaskModelAssembler = new AnnotationTaskModelAssembler();
        AnnotatorModelAssembler annotatorModelAssembler = new AnnotatorModelAssembler();

        AnnotationModel annotationModel = instantiateModel(entity);

        annotationModel.add(linkTo(
                methodOn(TaskController.class)
                        .getAnnotationById(entity.getAnnotationUUID()))
                .withSelfRel());

        annotationModel.setAnnotationUUID(entity.getAnnotationUUID());
        annotationModel.setAnnotationTask(annotationTaskModelAssembler.toModel(entity.getAnnotationTask()));
        annotationModel.setAnnotator(annotatorModelAssembler.toModel(entity.getAnnotator()));
        annotationModel.setData(entity.getData());
        annotationModel.setStatus(entity.getStatus());
        annotationModel.setSubmittedAt(entity.getSubmittedAt());
        annotationModel.setTask(taskModelAssembler.toModel(entity.getTask()));
        return annotationModel;
    }

}
