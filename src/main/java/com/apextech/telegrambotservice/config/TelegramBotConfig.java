package com.apextech.telegrambotservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class TelegramBotConfig {

    @Bean
    public RestClient telegramRestClient(
            @Value("${telegram.bot.api-url}") String apiUrl,
            @Value("${telegram.bot.token}") String token) {

        return RestClient.builder()
                .baseUrl(apiUrl + "/bot" + token)
                .build();
    }
}