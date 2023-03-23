package org.fgai4h.ap.domain.tool.mapper;

import org.fgai4h.ap.api.model.AnnotationToolDto;
import org.fgai4h.ap.domain.tool.model.AnnotationToolModel;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface AnnotationToolApiMapper {

    @Mapping(source = "id", target = "annotationToolUUID")
    AnnotationToolModel toAnnotationToolModel(AnnotationToolDto annotationDataDto);

    @InheritInverseConfiguration
    AnnotationToolDto toAnnotationToolDto(final AnnotationToolModel annotationDataModel);

}
