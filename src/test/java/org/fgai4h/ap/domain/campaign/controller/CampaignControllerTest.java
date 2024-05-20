package org.fgai4h.ap.domain.campaign.controller;

import org.fgai4h.ap.api.model.CampaignDto;
import org.fgai4h.ap.domain.campaign.model.CampaignModel;
import org.fgai4h.ap.domain.campaign.service.CampaignService;
import org.fgai4h.ap.domain.campaign.mapper.CampaignApiMapper;
import org.fgai4h.ap.domain.campaign.repository.CampaignRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
class CampaignControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CampaignService campaignService;

    @MockBean
    private CampaignApiMapper campaignApiMapper;

    @MockBean
    private CampaignRepository campaignRepository;

    @Autowired
    private CampaignController campaignController;

    @BeforeEach
    public void setup() {
        // Initialize your mocks here if needed
    }

    @Test
    void testGetAllCampaigns() throws Exception {
        mockMvc.perform(get("/campaigns"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return CREATED status when a campaign is successfully added")
    void shouldReturnCreatedWhenCampaignIsAdded() throws URISyntaxException {
        CampaignDto campaignDto = new CampaignDto();
        CampaignModel campaignModel = new CampaignModel();
        campaignModel.setCampaignUUID(UUID.randomUUID());

        when(campaignApiMapper.toCampaignModel(any(CampaignDto.class))).thenReturn(campaignModel);
        when(campaignService.addCampaign(any(CampaignModel.class))).thenReturn(campaignModel);

        ResponseEntity<Void> response = campaignController.addCampaign(campaignDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(new URI(campaignModel.getCampaignUUID().toString()), response.getHeaders().getLocation());
    }
}
