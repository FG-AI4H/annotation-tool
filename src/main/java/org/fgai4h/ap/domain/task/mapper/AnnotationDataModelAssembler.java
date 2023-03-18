package org.fgai4h.ap.domain.task.mapper;

import org.fgai4h.ap.domain.task.controller.TaskController;
import org.fgai4h.ap.domain.task.entity.AnnotationDataEntity;
import org.fgai4h.ap.domain.task.model.AnnotationDataModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AnnotationDataModelAssembler extends RepresentationModelAssemblerSupport<AnnotationDataEntity, AnnotationDataModel> {

    public AnnotationDataModelAssembler() {
        super(TaskController.class, AnnotationDataModel.class);
    }

    @Override
    public AnnotationDataModel toModel(AnnotationDataEntity entity) {
        AnnotationModelAssembler annotationModelAssembler = new AnnotationModelAssembler();
        AnnotationDataModel annotationDataModel = instantiateModel(entity);

        annotationDataModel.add(linkTo(
                methodOn(TaskController.class)
                        .getAnnotationDataById(entity.getAnnotationDataUUID()))
                .withSelfRel());

        annotationDataModel.setAnnotationDataUUID(entity.getAnnotationDataUUID());
        annotationDataModel.setData(entity.getData());
        annotationDataModel.setAnnotationModel(annotationModelAssembler.toModel(entity.getAnnotationEntity()));

        return annotationDataModel;
    }

}
