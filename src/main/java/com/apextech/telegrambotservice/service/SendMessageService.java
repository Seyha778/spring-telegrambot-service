package com.apextech.telegrambotservice.service;

public interface SendMessageService {

    String sendMessage(Long chatId, String message);
}
