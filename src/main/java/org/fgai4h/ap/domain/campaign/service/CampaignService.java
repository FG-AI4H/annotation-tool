package org.fgai4h.ap.domain.campaign.service;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.domain.campaign.entity.CampaignEntity;
import org.fgai4h.ap.domain.campaign.mapper.CampaignModelAssembler;
import org.fgai4h.ap.domain.campaign.model.CampaignModel;
import org.fgai4h.ap.domain.campaign.repository.CampaignRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final CampaignModelAssembler campaignModelAssembler;


    public List<CampaignModel> getAllCampaigns() {
        return campaignRepository.findAll().stream()
                .map(campaignModelAssembler::toModel).collect(Collectors.toList());
    }

    public CampaignModel addCampaign(CampaignModel campaignModel) {
        CampaignEntity newCampaign = campaignRepository.save(campaignModelAssembler.toEntity(campaignModel));
        return campaignModelAssembler.toModel(newCampaign);
    }
}
