package org.fgai4h.ap.domain.tool.mapper;

import org.fgai4h.ap.domain.tool.entity.AnnotationToolEntity;
import org.fgai4h.ap.domain.tool.model.AnnotationToolModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring")
public interface AnnotationToolMapper {

    AnnotationToolMapper INSTANCE = Mappers.getMapper(AnnotationToolMapper.class);

    AnnotationToolEntity toAnnotationToolEntity(AnnotationToolModel campaignModel);
    List<AnnotationToolEntity> toListAnnotationToolEntity(final List<AnnotationToolModel> annotationToolModelList);
}
