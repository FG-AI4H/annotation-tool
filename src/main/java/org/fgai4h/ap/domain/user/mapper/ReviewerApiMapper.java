package org.fgai4h.ap.domain.user.mapper;

import org.fgai4h.ap.api.model.ReviewerDto;
import org.fgai4h.ap.domain.user.model.ReviewerModel;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewerApiMapper {

    @Mapping(source = "id", target = "reviewerUUID")
    ReviewerModel toReviewerModel(ReviewerDto reviewerDto);

    @InheritInverseConfiguration
    ReviewerDto toReviewerDto(final ReviewerModel reviewerModel);
    
}
