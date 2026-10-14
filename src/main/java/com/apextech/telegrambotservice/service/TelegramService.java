package com.apextech.telegrambotservice.service;

import com.apextech.telegrambotservice.dto.request.TelegramRequest;

public interface TelegramService {

    String subscribe(TelegramRequest request);

    String unsubscribe(Long telegramUserId);

    boolean isSubscribed(Long telegramUserId);

    long countSubscribers();

}
