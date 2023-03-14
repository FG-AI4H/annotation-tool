package org.fgai4h.ap.domain.user.mapper;

import org.fgai4h.ap.domain.user.controller.UserController;
import org.fgai4h.ap.domain.user.model.UserModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;

@Component
public class UserModelAdminGetUserResponseAssembler extends RepresentationModelAssemblerSupport<AdminGetUserResponse, UserModel> {

    public UserModelAdminGetUserResponseAssembler() {
        super(UserController.class, UserModel.class);
    }

    @Override
    public UserModel toModel(AdminGetUserResponse entity) {
        UserModel userModel = instantiateModel(entity);

        for (AttributeType attribute : entity.userAttributes()) {
            switch (attribute.name()){
                case "sub":
                    userModel.setIdpID(attribute.value());
                    break;
                case "email":
                    userModel.setEmail(attribute.value());
                    break;
                default:
                    break;
            }
        }

        userModel.setUsername(entity.username());

        return userModel;
    }
}



