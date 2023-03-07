package org.fgai4h.ap.domain.campaign.controller;

import lombok.RequiredArgsConstructor;
import org.fgai4h.ap.api.CampaignApi;
import org.fgai4h.ap.api.model.CampaignDto;
import org.fgai4h.ap.domain.campaign.entity.CampaignEntity;
import org.fgai4h.ap.domain.campaign.mapper.CampaignApiMapper;
import org.fgai4h.ap.domain.campaign.mapper.CampaignModelAssembler;
import org.fgai4h.ap.domain.campaign.model.CampaignModel;
import org.fgai4h.ap.domain.campaign.repository.CampaignRepository;
import org.fgai4h.ap.domain.campaign.service.CampaignService;
import org.fgai4h.ap.domain.error.DomainError;
import org.fgai4h.ap.domain.exception.NotFoundException;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final CampaignModelAssembler campaignModelAssembler;
    private final CampaignService campaignService;
    private final CampaignApiMapper campaignApiMapper;

    @Override
    public ResponseEntity<List<CampaignDto>> getAllCampaigns() {
        return new ResponseEntity<>(
                campaignService.getAllCampaigns().stream().map(campaignApiMapper::toCampaignDto).collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CampaignDto> addCampaign(CampaignDto campaignDto) {
        CampaignModel campaignModel = campaignApiMapper.toCampaignModel(campaignDto);
        campaignModel = campaignService.addCampaign(campaignModel);
        return new ResponseEntity<CampaignDto>(campaignApiMapper.toCampaignDto(campaignModel),HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/campaigns/{id}")
    public ResponseEntity<CampaignModel> getCampaignById(@PathVariable("id") UUID id)
    {
        return campaignRepository.findById(id)
                .map(campaignModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/api/v1/campaigns/{id}")
    public ResponseEntity<?> updateCampaign(@RequestBody CampaignEntity campaign, @PathVariable UUID id){
        CampaignEntity campaignToUpdate = campaign;
        campaignToUpdate.setCampaignUUID(id);
        campaignRepository.save(campaignToUpdate);

        Link newlyCreatedLink = linkTo(methodOn(CampaignController.class).getCampaignById(id)).withSelfRel();

        try {
            return ResponseEntity.noContent().location(new URI(newlyCreatedLink.getHref())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body("Unable to update " + campaignToUpdate);
        }
    }


    @DeleteMapping("/api/v1/campaigns/{id}")
    public ResponseEntity<?> removeCampaign(@PathVariable UUID id){
        campaignRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/v1/campaigns/{id}/start")
    public ResponseEntity<?> start(@PathVariable UUID id) {

        CampaignEntity campaign = campaignRepository.findById(id).orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, "Campaign", "id", id));

        campaign.setStatus("STARTED");
        return ResponseEntity.ok(campaignRepository.save(campaign));
    }

    @PostMapping("/api/v1/campaigns/{id}/end")
    ResponseEntity<?> end(@PathVariable UUID id) {

        CampaignEntity campaign = campaignRepository.findById(id).orElseThrow(() -> new NotFoundException(DomainError.NOT_FOUND, "Campaign", "id", id));

        campaign.setStatus("ENDED");
        return ResponseEntity.ok(campaignRepository.save(campaign));
    }

}
