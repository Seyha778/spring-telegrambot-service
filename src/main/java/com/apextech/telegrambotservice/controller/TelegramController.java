package com.apextech.telegrambotservice.controller;

import com.apextech.telegrambotservice.dto.request.TelegramRequest;
import com.apextech.telegrambotservice.dto.response.ResponseMessage;
import com.apextech.telegrambotservice.dto.response.ResponseMessageBuilder;
import com.apextech.telegrambotservice.service.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/main")
@RequiredArgsConstructor
public class TelegramController {

    private final TelegramService telegramService;

    @PostMapping("/subscribe")
    public ResponseMessage<Object> subscribe(@RequestBody TelegramRequest request) {
        String message = telegramService.subscribe(request);
        return new ResponseMessageBuilder<>()
                .success()
                .data(message)
                .build();
    }

    @PostMapping("/unsubscribe")
    public ResponseMessage<Object> unsubscribe(@RequestBody TelegramRequest request) {
        String message = telegramService.unsubscribe(request.getTelegramUserId());
        return new ResponseMessageBuilder<>()
                .success()
                .data(message)
                .build();
    }

    @PostMapping("/status")
    public ResponseMessage<Object> getStatus(@RequestBody TelegramRequest request) {
        boolean subscribed = telegramService.isSubscribed(request.getTelegramUserId());
        return new ResponseMessageBuilder<>()
                .success()
                .data(subscribed)
                .build();
    }
}