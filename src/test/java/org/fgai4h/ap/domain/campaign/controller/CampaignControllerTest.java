package org.fgai4h.ap.domain.campaign.controller;

import org.fgai4h.ap.domain.campaign.controller.CampaignController;
import org.fgai4h.ap.domain.campaign.service.CampaignService;
import org.fgai4h.ap.domain.campaign.mapper.CampaignApiMapper;
import org.fgai4h.ap.domain.campaign.repository.CampaignRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CampaignControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CampaignService campaignService;

    @MockBean
    private CampaignApiMapper campaignApiMapper;

    @MockBean
    private CampaignRepository campaignRepository;

    @BeforeEach
    public void setup() {
        // Initialize your mocks here if needed
    }

    @Test
    public void testGetAllCampaigns() throws Exception {
        mockMvc.perform(get("/campaigns"))
                .andExpect(status().isOk());
    }

    // Add more test methods here for other endpoints
}
