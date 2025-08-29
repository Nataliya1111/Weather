package com.nataliya.integration.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("test")
public class TestFlywayConfig {

    @Bean
    public Flyway flyway(DataSource dataSource) {

        Flyway flyway = Flyway.configure().dataSource(dataSource).locations("classpath:db/migration_test").load();
        flyway.migrate();
        return flyway;
    }
}

