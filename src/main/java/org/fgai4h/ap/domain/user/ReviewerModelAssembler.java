package org.fgai4h.ap.domain.user;

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


        return reviewerModel;
    }
}
