package org.fgai4h.ap.domain.user.mapper;

import org.fgai4h.ap.domain.user.controller.UserController;
import org.fgai4h.ap.domain.user.entity.UserEntity;
import org.fgai4h.ap.domain.user.model.UserModel;
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
        SupervisorModelAssembler supervisorModelAssembler = new SupervisorModelAssembler();
        UserModel userModel = instantiateModel(entity);

        userModel.add(linkTo(
                methodOn(UserController.class)
                        .getUserById(entity.getUserUUID()))
                .withSelfRel());

        userModel.setUserUUID(entity.getUserUUID());
        userModel.setIdpID(entity.getIdpID());
        userModel.setUsername(entity.getUsername());
        userModel.setBirthdate(entity.getBirthdate());
        userModel.setAnnotatorRole(annotatorModelAssembler.toModel(entity.getAnnotatorRole()));
        userModel.setSupervisorRole(supervisorModelAssembler.toModel(entity.getSupervisorRole()));
        userModel.setReviewerRole(reviewerModelAssembler.toModel(entity.getReviewerRole()));

        return userModel;
    }

    public UserEntity toEntity(UserModel model){

        AnnotatorModelAssembler annotatorModelAssembler = new AnnotatorModelAssembler();
        ReviewerModelAssembler reviewerModelAssembler = new ReviewerModelAssembler();
        SupervisorModelAssembler supervisorModelAssembler = new SupervisorModelAssembler();

        UserEntity useEntity = new UserEntity();

        useEntity.setUserUUID(model.getUserUUID());
        useEntity.setIdpID(model.getIdpID());
        useEntity.setUsername(model.getUsername());
        if(model.getAnnotatorRole() != null) {
            useEntity.setAnnotatorRole(annotatorModelAssembler.toEntity(model.getAnnotatorRole()));
        }
        if(model.getReviewerRole() != null) {
            useEntity.setReviewerRole(reviewerModelAssembler.toEntity(model.getReviewerRole()));
        }
        if(model.getAnnotatorRole() != null) {
            useEntity.setSupervisorRole(supervisorModelAssembler.toEntity(model.getSupervisorRole()));
        }
        return useEntity;

    }
}

