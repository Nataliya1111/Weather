package com.nataliya.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({PersistenceConfig.class, FlywayConfig.class, SessionConfig.class})
@ComponentScan(basePackages = {"com.nataliya.service", "com.nataliya.mapper", "com.nataliya.repository"})
public class ApplicationConfig {

}
