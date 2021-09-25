package org.fgai4h.ap.domain.user;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AnnotatorModelAssembler extends RepresentationModelAssemblerSupport<AnnotatorEntity, AnnotatorModel> {

    public AnnotatorModelAssembler() {
        super(UserController.class, AnnotatorModel.class);
    }

    @Override
    public AnnotatorModel toModel(AnnotatorEntity entity) {
        if(isNull(entity)){
            return null;
        }

        AnnotatorModel annotatorModel = instantiateModel(entity);

        annotatorModel.add(linkTo(
                methodOn(UserController.class)
                        .getAnnotatorById(entity.getUserUUID()))
                .withSelfRel());

        annotatorModel.setUserUUID(entity.getUserUUID());
        annotatorModel.setExpertise(entity.getExpertise());
        annotatorModel.setUsername(entity.getUsername());

        return annotatorModel;
    }
}
