package org.fgai4h.ap.domain.campaign.mapper;

import org.fgai4h.ap.api.model.CampaignDto;
import org.fgai4h.ap.domain.campaign.model.CampaignModel;
import org.fgai4h.ap.domain.dataset.mapper.DatasetApiMapper;
import org.fgai4h.ap.domain.user.mapper.UserApiMapper;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", uses = {UserApiMapper.class, DatasetApiMapper.class})
public interface CampaignApiMapper {

    @Mapping(source = "id", target = "campaignUUID")
    CampaignModel toCampaignModel(CampaignDto campaignDto);

    List<CampaignModel> map(List<UUID> value);

    CampaignModel map(UUID value);

    @InheritInverseConfiguration
    CampaignDto toCampaignDto(final CampaignModel campaignModel);
}
