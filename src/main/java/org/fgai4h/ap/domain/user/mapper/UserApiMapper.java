package org.fgai4h.ap.domain.user.mapper;

import org.fgai4h.ap.api.model.UserDto;
import org.fgai4h.ap.domain.user.model.UserModel;
import org.fgai4h.ap.domain.user.service.UserService;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring",uses = {UserService.class, AnnotatorApiMapper.class, ReviewerApiMapper.class, SupervisorApiMapper.class})
public interface UserApiMapper {

    @Mapping(source = "id", target = "userUUID")
    UserModel toUserModel(UserDto userDto);

    default UUID map(UserModel userModel){
        if(userModel != null){
            return userModel.getUserUUID();
        }
        else{
            return null;
        }
    }

    @InheritInverseConfiguration
    UserDto toUserDto(final UserModel userModel);
}
