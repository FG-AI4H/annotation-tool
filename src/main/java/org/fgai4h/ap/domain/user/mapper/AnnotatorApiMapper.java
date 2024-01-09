package org.fgai4h.ap.domain.user.mapper;

import org.fgai4h.ap.api.model.AnnotatorDto;
import org.fgai4h.ap.domain.task.model.AnnotationModel;
import org.fgai4h.ap.domain.user.model.AnnotatorModel;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AnnotatorApiMapper {

    @Mapping(source = "id", target = "annotatorUUID")
    AnnotatorModel toAnnotatorModel(AnnotatorDto annotatorDto);

    default UUID map(AnnotationModel value){
        return value.getAnnotationUUID();
    }

    @InheritInverseConfiguration
    AnnotatorDto toAnnotatorDto(final AnnotatorModel annotatorModel);
}
