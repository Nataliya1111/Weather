package com.nataliya.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import({PersistenceConfig.class, FlywayConfig.class})
@ComponentScan(basePackages = {"com.nataliya.service", "com.nataliya.mapper"})
public class ApplicationConfig {

}
