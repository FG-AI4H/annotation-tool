package org.fgai4h.ap.domain.campaign;

import org.fgai4h.ap.domain.campaign.repository.CampaignRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DatabaseLoader {

    @Bean
    CommandLineRunner init(CampaignRepository repository) { // (1)

        return args -> { // (2)
            //repository.save(new CampaignEntity(UUID.randomUUID(),"Name1","grande mocha")); // (3)
            //repository.save(new CampaignEntity(UUID.randomUUID(),"Name2","venti hazelnut machiatto"));
        };
    }
}
