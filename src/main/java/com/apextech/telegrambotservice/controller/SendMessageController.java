package com.apextech.telegrambotservice.controller;

import com.apextech.telegrambotservice.dto.request.SendMessageRequest;
import com.apextech.telegrambotservice.dto.response.ResponseMessage;
import com.apextech.telegrambotservice.dto.response.ResponseMessageBuilder;
import com.apextech.telegrambotservice.service.SendMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/telegram")
@RequiredArgsConstructor
public class SendMessageController {

    private final SendMessageService sendMessageService;

    @PostMapping("/sendMessage")
    public ResponseMessage<Object> sendMessage(@RequestBody SendMessageRequest request) {
        String message = sendMessageService.sendMessage(
                Long.valueOf(request.getChatId()),
                request.getMessage());
        return new ResponseMessageBuilder<>()
                .success()
                .data(message)
                .build();
    }

}