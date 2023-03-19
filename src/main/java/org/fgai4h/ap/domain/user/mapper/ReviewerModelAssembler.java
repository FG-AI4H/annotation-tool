package org.fgai4h.ap.domain.user.mapper;

import org.fgai4h.ap.domain.user.controller.UserController;
import org.fgai4h.ap.domain.user.entity.ReviewerEntity;
import org.fgai4h.ap.domain.user.model.ReviewerModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

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

        reviewerModel.setReviewerUUID(entity.getReviewerUUID());
        reviewerModel.setExpectedSalary(entity.getExpectedSalary());
        reviewerModel.setSelfAssessment(entity.getSelfAssessment());


        return reviewerModel;
    }
}
