package com.apextech.telegrambotservice.service.impl;

import com.apextech.telegrambotservice.dto.request.TelegramRequest;
import com.apextech.telegrambotservice.enity.SubscriptionEntity;
import com.apextech.telegrambotservice.repository.SubscriptionRepository;
import com.apextech.telegrambotservice.service.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramServiceImpl implements TelegramService {

    private final SubscriptionRepository repository;
    private final RestClient telegramClient;

    @Override
    @Transactional
    public String subscribe(TelegramRequest request) {

        Long telegramUserId = request.getTelegramUserId();
        Long chatId = request.getChatId();
        String username = request.getUsername();
        String firstName = request.getFirstName();

        SubscriptionEntity subscription = repository
                .findByTelegramUserId(telegramUserId)
                .orElseGet(SubscriptionEntity::new);

        if (subscription.getId() != null && subscription.isSubscribed()) {
            log.info("Telegram user {} already subscribed.", telegramUserId);
            return "You are already subscribed.";
        }

        subscription.setTelegramUserId(telegramUserId);
        subscription.setChatId(chatId);
        subscription.setUsername(username);
        subscription.setFirstName(firstName);
        subscription.setSubscribed(true);

        repository.save(subscription);
        return "You have successfully subscribed!";
    }

    @Override
    @Transactional
    public String unsubscribe(Long telegramUserId) {
        return repository
                .findByTelegramUserId(telegramUserId)
                .map(subscription -> {
                    if (!subscription.isSubscribed()) {
                        log.info("Telegram user {} already unsubscribed.", telegramUserId);
                        return "You are already unsubscribed.";
                    }

                    subscription.setSubscribed(false);
                    repository.save(subscription);
                    log.info("Telegram user {} successfully unsubscribed", telegramUserId);
                    return "You have successfully unsubscribed.";
                })
                .orElseGet(() -> {
                    log.info("Telegram user {} is not subscribed", telegramUserId);
                    return "You are not subscribed.";
                });
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isSubscribed(Long telegramUserId) {
        return repository
                .existsByTelegramUserIdAndSubscribedTrue(
                        telegramUserId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countSubscribers() {
        return repository.countBySubscribedTrue();
    }
}
