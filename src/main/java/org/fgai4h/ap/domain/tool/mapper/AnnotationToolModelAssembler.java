package org.fgai4h.ap.domain.tool.mapper;

import org.fgai4h.ap.domain.task.mapper.AnnotationTaskModelAssembler;
import org.fgai4h.ap.domain.tool.controller.AnnotationToolController;
import org.fgai4h.ap.domain.tool.entity.AnnotationToolEntity;
import org.fgai4h.ap.domain.tool.model.AnnotationToolModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class AnnotationToolModelAssembler extends RepresentationModelAssemblerSupport<AnnotationToolEntity, AnnotationToolModel> {

    public AnnotationToolModelAssembler() {
        super(AnnotationToolController.class, AnnotationToolModel.class);
    }

    @Override
    public AnnotationToolModel toModel(AnnotationToolEntity entity) {
        AnnotationToolModel taskModel = instantiateModel(entity);

        AnnotationTaskModelAssembler annotationTaskModelAssembler = new AnnotationTaskModelAssembler();

        taskModel.add(linkTo(
                methodOn(AnnotationToolController.class)
                        .getAnnotationToolById(entity.getAnnotationToolUUID()))
                .withSelfRel());

        taskModel.setAnnotationToolUUID(entity.getAnnotationToolUUID());
        taskModel.setName(entity.getName());
        taskModel.setDescription(entity.getDescription());
        taskModel.setEditor(entity.getEditor());
        taskModel.setAnnotationTasks(annotationTaskModelAssembler.toAnnotationTaskModel(entity.getAnnotationTasks()));

        return taskModel;
    }
}

