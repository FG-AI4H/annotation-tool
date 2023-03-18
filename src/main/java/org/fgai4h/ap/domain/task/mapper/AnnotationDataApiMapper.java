package org.fgai4h.ap.domain.task.mapper;

import org.fgai4h.ap.api.model.AnnotationDataDto;
import org.fgai4h.ap.domain.task.model.AnnotationDataModel;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnnotationDataApiMapper {

    @Mapping(source = "id", target = "annotationDataUUID")
    AnnotationDataModel toAnnotationDataModel(AnnotationDataDto annotationDataDto);

    @InheritInverseConfiguration
    AnnotationDataDto toAnnotationDataDto(final AnnotationDataModel annotationDataModel);

}
