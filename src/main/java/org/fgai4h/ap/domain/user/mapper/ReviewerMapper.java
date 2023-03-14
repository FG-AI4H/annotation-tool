package org.fgai4h.ap.domain.user.mapper;

import org.fgai4h.ap.domain.user.entity.ReviewerEntity;
import org.fgai4h.ap.domain.user.model.ReviewerModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewerMapper {

    ReviewerMapper INSTANCE = Mappers.getMapper(ReviewerMapper.class);

    ReviewerEntity toReviewerEntity(ReviewerModel reviewerModel);
    List<ReviewerEntity> toListReviewerEntity(final List<ReviewerModel> reviewerModelList);
}
