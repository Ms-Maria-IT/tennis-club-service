package com.tennistournament.clubservice.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {
    // RequestLoggingFilter is already configured as a @Component
    // No need for duplicate CommonsRequestLoggingFilter bean
}