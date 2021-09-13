package org.fgai4h.ap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class AnnotationBackendApplication {


    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(AnnotationBackendApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/oauth2/authorization/cognito").allowedOrigins("http://localhost:3000");
            }
        };
    }


}
