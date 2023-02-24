package ru.practicum.ewm.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.client.HitClient;

@Configuration
public class HitClientConfig {
    @Value("${stats-service.url}")
    private String url;

    @Bean
    HitClient hitClient() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        return new HitClient(url, builder);
    }
}
