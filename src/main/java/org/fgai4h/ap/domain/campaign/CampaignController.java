package org.fgai4h.ap.domain.campaign;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
public class CampaignController {

    private final CampaignRepository campaignRepository;
    private final CampaignModelAssembler campaignModelAssembler;

    public CampaignController(CampaignRepository campaignRepository, CampaignModelAssembler campaignModelAssembler) {
        this.campaignRepository = campaignRepository;
        this.campaignModelAssembler = campaignModelAssembler;
    }


    @GetMapping("/api/v1/campaigns")
    public ResponseEntity<CollectionModel<CampaignModel>> getAllCampaigns()
    {
        List<CampaignEntity> campaignEntities = campaignRepository.findAll();
        return new ResponseEntity<>(
                campaignModelAssembler.toCollectionModel(campaignEntities),
                HttpStatus.OK);
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

    @PostMapping("/api/v1/campaigns")
    public ResponseEntity<?> addCampaign(@RequestBody CampaignEntity newCampaign){
        newCampaign = campaignRepository.save(newCampaign);

        Link newlyCreatedLink = linkTo(methodOn(CampaignController.class).getCampaignById(newCampaign.getCampaignUUID())).withSelfRel();

        try {
            return ResponseEntity.noContent().location(new URI(newlyCreatedLink.getHref())).build();
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body("Unable to add " + newCampaign);
        }
    }

    @DeleteMapping("/api/v1/campaigns/{id}")
    public ResponseEntity<?> removeCampaign(@PathVariable UUID id){
        campaignRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/v1/campaigns/{id}/start")
    ResponseEntity<?> start(@PathVariable UUID id) {

        CampaignEntity campaign = campaignRepository.findById(id).orElseThrow(() -> new CampaignNotFoundException(id));

        campaign.setStatus("STARTED");
        return ResponseEntity.ok(campaignRepository.save(campaign));
    }

    @PostMapping("/api/v1/campaigns/{id}/end")
    ResponseEntity<?> start(@PathVariable UUID id) {

        CampaignEntity campaign = campaignRepository.findById(id).orElseThrow(() -> new CampaignNotFoundException(id));

        campaign.setStatus("ENDED");
        return ResponseEntity.ok(campaignRepository.save(campaign));
    }

}
