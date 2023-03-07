package org.fgai4h.ap.domain.user.mapper;

import org.fgai4h.ap.api.model.SupervisorDto;
import org.fgai4h.ap.domain.user.model.SupervisorModel;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SupervisorApiMapper {

    @Mapping(source = "id", target = "supervisorUUID")
    SupervisorModel toSupervisorModel(SupervisorDto supervisorDto);

    @InheritInverseConfiguration
    SupervisorDto toSupervisorDto(final SupervisorModel supervisorModel);
}
