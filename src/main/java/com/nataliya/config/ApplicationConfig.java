package com.nataliya.config;

import org.springframework.context.annotation.Import;

@Import({PersistenceConfig.class, FlywayConfig.class})
public class ApplicationConfig {

}
