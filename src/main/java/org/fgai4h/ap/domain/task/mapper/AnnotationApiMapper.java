package org.fgai4h.ap.domain.task.mapper;

import org.fgai4h.ap.api.model.AnnotationDto;
import org.fgai4h.ap.domain.task.model.AnnotationDataModel;
import org.fgai4h.ap.domain.task.model.AnnotationModel;
import org.fgai4h.ap.domain.task.service.TaskService;
import org.fgai4h.ap.domain.user.mapper.UserApiMapper;
import org.fgai4h.ap.domain.user.service.UserService;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = {UserService.class, TaskService.class, UserApiMapper.class})
public interface AnnotationApiMapper {

    @Mapping(source = "id", target = "annotationUUID")
    AnnotationModel toAnnotationModel(AnnotationDto annotationDto);

    List<AnnotationDataModel> toAnnotationModelList(List<UUID> value);
    List<UUID> toUUIDList(List<AnnotationDataModel> value);

    default UUID map(AnnotationDataModel value){
        return value.getAnnotationDataUUID();
    }

    @InheritInverseConfiguration
    AnnotationDto toAnnotationDto(final AnnotationModel annotationModel);

}
