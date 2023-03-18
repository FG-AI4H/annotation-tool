package org.fgai4h.ap.domain.user.mapper;

import org.fgai4h.ap.domain.user.controller.UserController;
import org.fgai4h.ap.domain.user.model.UserModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;

import static java.util.Objects.isNull;

@Component
public class UserModelAWSAssembler extends RepresentationModelAssemblerSupport<UserType, UserModel> {

    public UserModelAWSAssembler() {
        super(UserController.class, UserModel.class);
    }

    @Override
    public UserModel toModel(UserType entity) {
        if(isNull(entity)){
            return null;
        }

        UserModel userModel = instantiateModel(entity);

        for (AttributeType attribute : entity.attributes()) {
            switch (attribute.name()){
                case "sub": userModel.setIdpId(attribute.value());
                    break;
                case "email": userModel.setEmail(attribute.value());
                    break;
                default: break;
            }
        }

        userModel.setUsername(entity.username());

        return userModel;
    }
}


