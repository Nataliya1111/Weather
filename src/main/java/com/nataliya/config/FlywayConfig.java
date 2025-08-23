package com.nataliya.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class FlywayConfig {

    @Value("${hibernate.default_schema}")
    private String schema;

    @Bean
    public Flyway flyway(DataSource dataSource) {

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("schema", schema);

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .defaultSchema(schema)
                .placeholders(placeholders)
                .load();
        flyway.migrate();

        return flyway;
    }
}
