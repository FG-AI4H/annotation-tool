package org.fgai4h.ap.domain.campaign.mapper;

import org.fgai4h.ap.api.model.CampaignDto;
import org.fgai4h.ap.domain.campaign.model.CampaignModel;
import org.fgai4h.ap.domain.campaign.service.CampaignService;
import org.fgai4h.ap.domain.dataset.mapper.DatasetApiMapper;
import org.fgai4h.ap.domain.user.mapper.UserApiMapper;
import org.fgai4h.ap.domain.user.service.UserService;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = {UserService.class,CampaignService.class, UserApiMapper.class, DatasetApiMapper.class})
public interface CampaignApiMapper {

    @Mapping(source = "id", target = "campaignUUID")
    CampaignModel toCampaignModel(CampaignDto campaignDto);

    default UUID map(CampaignModel value){
        return  value.getCampaignUUID();
    }

    @InheritInverseConfiguration
    CampaignDto toCampaignDto(final CampaignModel campaignModel);

}
