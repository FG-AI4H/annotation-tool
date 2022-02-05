package org.fgai4h.ap.domain.user;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler extends RepresentationModelAssemblerSupport<UserEntity, UserModel> {

    public UserModelAssembler() {
        super(UserController.class, UserModel.class);
    }

    @Override
    public UserModel toModel(UserEntity entity) {
        if(isNull(entity)){
            return null;
        }

        AnnotatorModelAssembler annotatorModelAssembler = new AnnotatorModelAssembler();
        ReviewerModelAssembler reviewerModelAssembler = new ReviewerModelAssembler();
        UserModel userModel = instantiateModel(entity);

        userModel.add(linkTo(
                methodOn(UserController.class)
                        .getUserById(entity.getUserUUID()))
                .withSelfRel());

        userModel.setUserUUID(entity.getUserUUID());
        userModel.setIdpID(entity.getIdpID());
        userModel.setUsername(entity.getUsername());
        userModel.setAnnotatorRole(annotatorModelAssembler.toModel(entity.getAnnotatorRole()));

        userModel.setReviewerRole(reviewerModelAssembler.toModel(entity.getReviewerRole()));

        return userModel;
    }

    public  UserEntity toEntity(UserModel model){

        AnnotatorModelAssembler annotatorModelAssembler = new AnnotatorModelAssembler();

        UserEntity useEntity = new UserEntity();

        useEntity.setUserUUID(model.getUserUUID());
        useEntity.setIdpID(model.getIdpID());
        useEntity.setUsername(model.getUsername());
        if(model.getAnnotatorRole() != null) {
            useEntity.setAnnotatorRole(annotatorModelAssembler.toEntity(model.getAnnotatorRole()));
        }
        return useEntity;

    }
}

