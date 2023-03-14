package org.fgai4h.ap.domain.campaign.controller;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.api.CampaignApi;
import org.fgai4h.ap.api.model.CampaignDto;
import org.fgai4h.ap.domain.campaign.mapper.CampaignApiMapper;
import org.fgai4h.ap.domain.campaign.model.CampaignModel;
import org.fgai4h.ap.domain.campaign.repository.CampaignRepository;
import org.fgai4h.ap.domain.campaign.service.CampaignService;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequiredArgsConstructor
public class CampaignController implements CampaignApi {

    private final CampaignRepository campaignRepository;
    private final CampaignService campaignService;
    private final CampaignApiMapper campaignApiMapper;

    @Override
    public ResponseEntity<List<CampaignDto>> getAllCampaigns() {
        return new ResponseEntity<>(
                campaignService.getAllCampaigns().stream().map(campaignApiMapper::toCampaignDto).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> addCampaign(CampaignDto campaignDto) {
        CampaignModel campaignModel = campaignApiMapper.toCampaignModel(campaignDto);
        campaignModel = campaignService.addCampaign(campaignModel);

        try {
            return ResponseEntity.created(new URI(campaignModel.getCampaignUUID().toString())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<CampaignDto> getCampaignById(UUID campaignId) {
        return campaignService.getCampaignById(campaignId)
                .map(campaignApiMapper::toCampaignDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @Override
    public ResponseEntity<Void> updateCampaign(UUID campaignId, CampaignDto campaignDto) {
        CampaignModel campaignModel = campaignApiMapper.toCampaignModel(campaignDto);
        campaignService.updateCampaign(campaignModel);

        Link newlyCreatedLink = linkTo(methodOn(CampaignController.class).getCampaignById(campaignId)).withSelfRel();

        try {
            return ResponseEntity.noContent().location(new URI(newlyCreatedLink.getHref())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<Void> startCampaign(UUID campaignId) {
        campaignService.startCampaign(campaignId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<Void> deleteCampaignById(UUID campaignId) {
        campaignService.deleteCampaignById(campaignId);
        return ResponseEntity.noContent().build();
    }
}
