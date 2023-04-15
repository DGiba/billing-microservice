package org.alfabet.exercise.integrations.processor.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
public class RestConfiguration {

    @Bean
    public RestTemplate processorRestTemplate() {
        return new RestTemplate();
    }
}
