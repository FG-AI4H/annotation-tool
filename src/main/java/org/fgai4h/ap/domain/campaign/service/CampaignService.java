package org.fgai4h.ap.domain.campaign.service;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.api.model.CampaignStatus;
import org.fgai4h.ap.domain.campaign.entity.CampaignEntity;
import org.fgai4h.ap.domain.campaign.mapper.CampaignMapper;
import org.fgai4h.ap.domain.campaign.mapper.CampaignModelAssembler;
import org.fgai4h.ap.domain.campaign.model.CampaignModel;
import org.fgai4h.ap.domain.campaign.model.CampaignStatusModel;
import org.fgai4h.ap.domain.campaign.repository.CampaignRepository;
import org.fgai4h.ap.domain.error.DomainError;
import org.fgai4h.ap.domain.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final CampaignModelAssembler campaignModelAssembler;
    private final CampaignMapper campaignMapper;


    public List<CampaignModel> getAllCampaigns() {
        return campaignRepository.findAll().stream()
                .map(campaignModelAssembler::toModel).collect(Collectors.toList());
    }

    public CampaignModel addCampaign(CampaignModel campaignModel) {
        campaignModel.setStatus(CampaignStatusModel.DRAFT);
        CampaignEntity newCampaign = campaignRepository.save(campaignMapper.toCampaignEntity(campaignModel));
        return campaignModelAssembler.toModel(newCampaign);
    }

    public Optional<CampaignModel> getCampaignById(UUID campaignId) {
        return Optional.ofNullable(campaignRepository
                .findById(campaignId)
                .map(campaignModelAssembler::toModel)
                .orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, "Campaign", "id", campaignId)));
    }

    public CampaignModel findById(UUID campaignId){
        return getCampaignById(campaignId).orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, "Campaign", "id", campaignId));
    }

    public void updateCampaign(CampaignModel campaignModel) {
        campaignRepository.save(campaignMapper.toCampaignEntity(campaignModel));
    }

    public void startCampaign(UUID campaignId) {
        CampaignEntity campaign = campaignRepository.findById(campaignId).orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, "Campaign", "id", campaignId));
        campaign.setStatus(CampaignStatus.RUNNING.toString());
        campaignRepository.save(campaign);
    }
}
