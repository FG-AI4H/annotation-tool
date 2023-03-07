package org.fgai4h.ap.domain.user.mapper;

import org.fgai4h.ap.api.model.UserDto;
import org.fgai4h.ap.domain.user.model.UserModel;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring",uses = {AnnotatorApiMapper.class, ReviewerApiMapper.class, SupervisorApiMapper.class})
public interface UserApiMapper {

    @Mapping(source = "id", target = "userUUID")
    UserModel toUserModel(UserDto userDto);

    List<UserModel> toListUserModel(List<UUID> value);
    List<UUID> toListUUID(List<UserModel> users);

    default UUID map(UserModel userModel){
        return userModel.getUserUUID();
    }

    default UserModel map(UUID uuid){
        return UserModel.builder().userUUID(uuid).build();
    }

    @InheritInverseConfiguration
    UserDto toUserDto(final UserModel userModel);
}
