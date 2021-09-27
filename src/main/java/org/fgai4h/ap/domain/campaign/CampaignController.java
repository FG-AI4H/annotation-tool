package org.fgai4h.ap.domain.campaign;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
public class CampaignController {

    private final CampaignRepository campaignRepository;
    private final CampaignModelAssembler campaignModelAssembler;

    public CampaignController(CampaignRepository campaignRepository, CampaignModelAssembler campaignModelAssembler) {
        this.campaignRepository = campaignRepository;
        this.campaignModelAssembler = campaignModelAssembler;
    }


    @GetMapping("/campaigns")
    public ResponseEntity<CollectionModel<CampaignModel>> getAllCampaigns()
    {
        List<CampaignEntity> campaignEntities = campaignRepository.findAll();
        return new ResponseEntity<>(
                campaignModelAssembler.toCollectionModel(campaignEntities),
                HttpStatus.OK);
    }


    @GetMapping("/campaigns/{id}")
    public ResponseEntity<CampaignModel> getCampaignById(@PathVariable("id") UUID id)
    {
        return campaignRepository.findById(id)
                .map(campaignModelAssembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/campaigns/{id}/start")
    ResponseEntity<?> start(@PathVariable UUID id) {

        CampaignEntity campaign = campaignRepository.findById(id).orElseThrow(() -> new CampaignNotFoundException(id));

        campaign.setStatus("STARTED");
        return ResponseEntity.ok(campaignRepository.save(campaign));
    }

}
