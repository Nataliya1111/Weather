package com.nataliya.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@Import({PersistenceConfig.class,
        DataSourceConfig.class,
        FlywayConfig.class,
        SessionConfig.class,
        HttpClientConfig.class,
        JacksonConfig.class})
@ComponentScan(basePackages = {"com.nataliya.service", "com.nataliya.mapper", "com.nataliya.repository"})
@EnableScheduling
public class ApplicationConfig {

}
