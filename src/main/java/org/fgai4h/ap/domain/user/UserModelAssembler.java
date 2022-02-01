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

        UserModel userModel = instantiateModel(entity);

        userModel.add(linkTo(
                methodOn(UserController.class)
                        .getUserById(entity.getUserUUID()))
                .withSelfRel());

        userModel.setUserUUID(entity.getUserUUID());
        userModel.setIdpID(entity.getIdpID());
        userModel.setUsername(entity.getUsername());

        return userModel;
    }
}

