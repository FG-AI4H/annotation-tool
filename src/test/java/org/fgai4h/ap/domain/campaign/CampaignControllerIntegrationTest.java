package org.fgai4h.ap.domain.campaign;

import org.fgai4h.ap.domain.campaign.controller.CampaignController;
import org.fgai4h.ap.domain.campaign.entity.CampaignEntity;
import org.fgai4h.ap.domain.campaign.mapper.CampaignModelAssembler;
import org.fgai4h.ap.domain.campaign.repository.CampaignRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CampaignController.class)
public class CampaignControllerIntegrationTest {

    @TestConfiguration
    static class CampaignControllerIntegrationTestConfiguration {

        @Bean
        public CampaignModelAssembler campaignModelAssembler() {
            return new CampaignModelAssembler();
        }
    }

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CampaignRepository repository;

    CampaignEntity campaign1 = new CampaignEntity(UUID.randomUUID(),"Name1","grande mocha");
    CampaignEntity campaign2 = new CampaignEntity(UUID.randomUUID(),"Name2","venti hazelnut machiatto");

    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    public void givenCampaigns_whenGetCampaigns_thenReturnJsonArray()
            throws Exception {

        List<CampaignEntity> allCampaigns = new ArrayList<>(Arrays.asList(campaign1, campaign2));

        given(repository.findAll()).willReturn(allCampaigns);
        mvc.perform(MockMvcRequestBuilders.get("/api/v1/campaigns")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.campaign", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.campaign[0].name", is(campaign1.getName())));
    }

}
