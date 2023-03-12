package org.fgai4h.ap.domain.campaign.mapper;

import org.fgai4h.ap.domain.campaign.entity.CampaignEntity;
import org.fgai4h.ap.domain.campaign.model.CampaignModel;
import org.fgai4h.ap.domain.dataset.mapper.DatasetMapper;
import org.fgai4h.ap.domain.user.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, DatasetMapper.class, ClassLabelApiMapper.class})
public interface CampaignMapper {

    CampaignMapper INSTANCE = Mappers.getMapper(CampaignMapper.class);

    CampaignEntity toCampaignEntity(CampaignModel campaignModel);
    List<CampaignEntity> toListCampaignEntity(final List<CampaignModel> campaignModelList);
}
