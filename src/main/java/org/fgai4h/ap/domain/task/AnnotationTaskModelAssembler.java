package org.fgai4h.ap.domain.task;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AnnotationTaskModelAssembler extends RepresentationModelAssemblerSupport<AnnotationTaskEntity, AnnotationTaskModel> {

    public AnnotationTaskModelAssembler() {
        super(TaskController.class, AnnotationTaskModel.class);
    }


    @Override
    public AnnotationTaskModel toModel(AnnotationTaskEntity entity) {
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
}
