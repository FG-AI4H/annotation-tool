package org.fgai4h.ap.domain.task;

import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoaderTask {

    @Bean
    public Jackson2RepositoryPopulatorFactoryBean getRespositoryPopulator() {
        Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
        //factory.setResources(new Resource[]{new ClassPathResource("whoTask1.json")});
        //factory.setResources(new Resource[]{new ClassPathResource("whoTask2.json")});
        //factory.setResources(new Resource[]{new ClassPathResource("Annotations3.json")});
        //factory.setResources(new Resource[]{new ClassPathResource("sample3.json")});
        //factory.setResources(new Resource[]{new ClassPathResource("sample1.json")});
        //factory.setResources(new Resource[]{new ClassPathResource("sample2.json")});
        return factory;
    }
}
