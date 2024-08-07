package org.fgai4h.ap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

import static org.fgai4h.ap.helpers.SecretsManager.getSecret;

@Configuration
@Profile("prod")
public class DataSourceConfig {

        private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);

        @Bean
        public DataSource getDataSource() {
                DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
                dataSourceBuilder.url("jdbc:mysql://fg-ai4h-db.cluster-cllejqtoj5fh.eu-central-1.rds.amazonaws.com:3306/fg_ai4h");
                String secret = getSecret();
                ObjectMapper objectMapper  =  new  ObjectMapper();
                JsonNode secretsJson  = null;
                try {
                        secretsJson = objectMapper.readTree(secret);
                        dataSourceBuilder.username(secretsJson.get("username").textValue());
                        dataSourceBuilder.password(secretsJson.get("password").textValue());

                } catch (JsonProcessingException e) {
                        LOGGER.error("Error processing JSON", e);
                }

                return dataSourceBuilder.build();
        }
}
