package org.fgai4h.ap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AnnotationBackendApplication {


    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(AnnotationBackendApplication.class, args);
    }

}
