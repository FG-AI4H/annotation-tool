package org.fgai4h.ap.domain.campaign.mapper;


import org.fgai4h.ap.api.model.ClassLabelDto;
import org.fgai4h.ap.domain.campaign.model.ClassLabelModel;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClassLabelApiMapper {

    @Mapping(source = "id", target = "classLabelUUID")
    ClassLabelModel toClassLabelModel(ClassLabelDto classLabelDto);

    @InheritInverseConfiguration
    ClassLabelDto toClassLabelDto(final ClassLabelModel classLabelModel);

}
