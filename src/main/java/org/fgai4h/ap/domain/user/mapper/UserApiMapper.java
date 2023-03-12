package org.fgai4h.ap.domain.user.mapper;

import org.fgai4h.ap.api.model.UserDto;
import org.fgai4h.ap.domain.user.model.UserModel;
import org.fgai4h.ap.domain.user.service.UserService;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",uses = {UserService.class, AnnotatorApiMapper.class, ReviewerApiMapper.class, SupervisorApiMapper.class})
public interface UserApiMapper {

    @Autowired
    UserService userService = null;

    @Mapping(source = "id", target = "userUUID")
    UserModel toUserModel(UserDto userDto);

    List<UUID> toListUUID(List<UserModel> users);

    default UUID map(UserModel userModel){
        return userModel.getUserUUID();
    }

    default List<UserModel> toUserModelList(List<UUID> idList){
        List<UserModel> list = new ArrayList<UserModel>();
        if(idList != null) {
            list = idList.stream().map(id -> userService.getUserById(id).get()).collect(Collectors.toList());
        }

        return list;
    }

    @InheritInverseConfiguration
    UserDto toUserDto(final UserModel userModel);
}
