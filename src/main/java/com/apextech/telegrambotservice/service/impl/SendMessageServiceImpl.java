package com.apextech.telegrambotservice.service.impl;

import com.apextech.telegrambotservice.service.SendMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendMessageServiceImpl implements SendMessageService {

    private final RestClient telegramClient;

    @Override
    public String sendMessage(Long chatId, String message) {
        telegramClient.post()
                .uri("/sendMessage")
                .body(Map.of(
                        "chat_id", chatId,
                        "text", message
                ))
                .retrieve()
                .toBodilessEntity();
        log.info("Message sent successfully | chatId={}, message={}", chatId, message);
        return "Message sent successfully";
    }
}
