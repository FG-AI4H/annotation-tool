package org.fgai4h.ap.domain.campaign;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

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

        campaignModel.add(linkTo(
                methodOn(CampaignController.class)
                        .getCampaignById(entity.getCampaignUUID()))
                .withSelfRel());

        // If PAID_FOR is valid, add a link to the `pay()` method
        if (!"PAID".equals(entity.getName())) {
            campaignModel.add(linkTo(
                    methodOn(CampaignController.class).pay(entity.getCampaignUUID()))
                            .withRel(IanaLinkRelations.PAYMENT));
        }

        campaignModel.setCampaignUUID(entity.getCampaignUUID());
        campaignModel.setName(entity.getName());
        campaignModel.setDescription(entity.getDescription());
        return campaignModel;
    }

    @Override
    public CollectionModel<CampaignModel> toCollectionModel(Iterable<? extends CampaignEntity> entities)
    {
        CollectionModel<CampaignModel> campaignModels = super.toCollectionModel(entities);

        campaignModels.add(linkTo(methodOn(CampaignController.class).getAllCampaigns()).withSelfRel());

        return campaignModels;
    }

}
