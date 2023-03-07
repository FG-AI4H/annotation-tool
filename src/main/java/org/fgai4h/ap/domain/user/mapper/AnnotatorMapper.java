package org.fgai4h.ap.domain.user.mapper;

import org.fgai4h.ap.domain.user.entity.AnnotatorEntity;
import org.fgai4h.ap.domain.user.model.AnnotatorModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnnotatorMapper {

    AnnotatorMapper INSTANCE = Mappers.getMapper(AnnotatorMapper.class);

    AnnotatorEntity toAnnotatorEntity(AnnotatorModel annotatorModel);
    List<AnnotatorEntity> toListAnnotatorEntity(final List<AnnotatorModel> annotatorModelList);
}
