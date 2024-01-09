package org.fgai4h.ap.domain.task.mapper;

import org.fgai4h.ap.api.model.AnnotationTaskDto;
import org.fgai4h.ap.domain.task.model.AnnotationTaskModel;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AnnotationTaskApiMapper {

    @Mapping(source = "id", target = "annotationTaskUUID")
    AnnotationTaskModel toAnnotationTaskModel(AnnotationTaskDto annotationTaskDto);

    @InheritInverseConfiguration
    AnnotationTaskDto toAnnotationTaskDto(final AnnotationTaskModel annotationTaskModel);

    default UUID map(AnnotationTaskModel annotationTaskModel){
        return annotationTaskModel.getAnnotationTaskUUID();
    }

}
