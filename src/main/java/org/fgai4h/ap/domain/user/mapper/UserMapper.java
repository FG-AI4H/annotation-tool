package org.fgai4h.ap.domain.user.mapper;

import org.fgai4h.ap.domain.user.entity.UserEntity;
import org.fgai4h.ap.domain.user.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",uses = {AnnotatorMapper.class, ReviewerMapper.class, SupervisorMapper.class})
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserEntity toUserEntity(UserModel userModel);
    List<UserEntity> toListUserEntity(final List<UserModel> userModelList);
}
