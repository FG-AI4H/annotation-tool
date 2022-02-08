package org.fgai4h.ap.domain.campaign;

import org.fgai4h.ap.domain.user.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CampaignModelAssembler extends RepresentationModelAssemblerSupport<CampaignEntity, CampaignModel> {

    public CampaignModelAssembler() {
        super(CampaignController.class, CampaignModel.class);
    }

    @Override
    public CampaignModel toModel(CampaignEntity entity)
    {
        CampaignModel campaignModel = instantiateModel(entity);
        UserModelAssembler userModelAssembler = new UserModelAssembler();

        campaignModel.add(linkTo(
                methodOn(CampaignController.class)

                        .getCampaignById(entity.getCampaignUUID()))
                .withSelfRel());

        // If PAID_FOR is valid, add a link to the `pay()` method
        if (!"PAID".equals(entity.getName())) {
            campaignModel.add(linkTo(
                    methodOn(CampaignController.class).start(entity.getCampaignUUID()))
                            .withRel(IanaLinkRelations.PAYMENT));
        }

        campaignModel.setCampaignUUID(entity.getCampaignUUID());
        campaignModel.setName(entity.getName());
        campaignModel.setDescription(entity.getDescription());
        campaignModel.setStatus(entity.getStatus());
        campaignModel.setDatasets(entity.getDatasets());
        campaignModel.setAnnotators(toUserModel(entity.getAnnotators()));
        campaignModel.setReviewers(toUserModel(entity.getReviewers()));

        return campaignModel;
    }

    private List<UserModel> toUserModel(List<UserEntity> users) {
        if (users.isEmpty())
            return Collections.emptyList();

        AnnotatorModelAssembler annotatorModelAssembler = new AnnotatorModelAssembler();
        ReviewerModelAssembler reviewerModelAssembler = new ReviewerModelAssembler();

        return users.stream()
                .map(user-> UserModel.builder()
                        .userUUID(user.getUserUUID())
                        .username(user.getUsername())
                        .idpID(user.getIdpID())
                        .annotatorRole(annotatorModelAssembler.toModel(user.getAnnotatorRole()))
                        .reviewerRole(reviewerModelAssembler.toModel(user.getReviewerRole()))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public CollectionModel<CampaignModel> toCollectionModel(Iterable<? extends CampaignEntity> entities)
    {
        CollectionModel<CampaignModel> campaignModels = super.toCollectionModel(entities);

        campaignModels.add(linkTo(methodOn(CampaignController.class).getAllCampaigns()).withSelfRel());

        return campaignModels;
    }

}
