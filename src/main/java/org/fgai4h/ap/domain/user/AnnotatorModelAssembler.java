package org.fgai4h.ap.domain.user;

import org.fgai4h.ap.domain.task.AnnotationTaskEntity;
import org.fgai4h.ap.domain.task.AnnotationTaskModel;
import org.fgai4h.ap.domain.task.TaskController;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class AnnotatorModelAssembler extends RepresentationModelAssemblerSupport<AnnotatorEntity, AnnotatorModel> {

    public AnnotatorModelAssembler() {
        super(UserController.class, AnnotatorModel.class);
    }

    @Override
    public AnnotatorModel toModel(AnnotatorEntity entity) {
        return null;
    }
}
