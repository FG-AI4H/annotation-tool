package org.fgai4h.ap.domain.user.mapper;

import org.fgai4h.ap.domain.user.entity.SupervisorEntity;
import org.fgai4h.ap.domain.user.model.SupervisorModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SupervisorMapper {

    SupervisorMapper INSTANCE = Mappers.getMapper(SupervisorMapper.class);

    SupervisorEntity toSupervisorEntity(SupervisorModel supervisorModel);
    List<SupervisorEntity> toListSupervisorEntity(final List<SupervisorModel> supervisorModelList);
}
