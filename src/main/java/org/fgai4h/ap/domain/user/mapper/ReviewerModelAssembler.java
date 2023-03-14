package org.fgai4h.ap.domain.user.mapper;

import org.fgai4h.ap.domain.user.controller.UserController;
import org.fgai4h.ap.domain.user.entity.ReviewerEntity;
import org.fgai4h.ap.domain.user.model.ReviewerModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReviewerModelAssembler extends RepresentationModelAssemblerSupport<ReviewerEntity, ReviewerModel> {

    public ReviewerModelAssembler() {
        super(UserController.class, ReviewerModel.class);
    }

    @Override
    public ReviewerModel toModel(ReviewerEntity entity) {
        if(isNull(entity)){
            return null;
        }

        ReviewerModel reviewerModel = instantiateModel(entity);

        reviewerModel.add(linkTo(
                methodOn(UserController.class)
                        .getReviewerById(entity.getReviewerUUID()))
                .withSelfRel());


        return reviewerModel;
    }

    public List<ReviewerModel> toReviewerModel(List<ReviewerEntity> reviewers) {
        if (reviewers.isEmpty())
            return Collections.emptyList();

        return reviewers.stream()
                .map(reviewer-> ReviewerModel.builder()
                        .reviewerUUID(reviewer.getReviewerUUID())
                        .build())
                .collect(Collectors.toList());
    }

    public ReviewerEntity toEntity(ReviewerModel reviewerRole) {
        ReviewerEntity entity = new ReviewerEntity();
        entity.setReviewerUUID(reviewerRole.getReviewerUUID());

        return entity;
    }
}
