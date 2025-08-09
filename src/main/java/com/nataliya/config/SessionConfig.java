package com.nataliya.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.time.Duration;

@Configuration
@PropertySource("classpath:application.properties")
public class SessionConfig {

    @Bean
    public Duration sessionTimeout(@Value("${session.timeout}") long sessionTimeout) {
        return Duration.ofSeconds(sessionTimeout);
    }
}
