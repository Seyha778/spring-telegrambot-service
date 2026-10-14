package com.apextech.telegrambotservice.repository;

import com.apextech.telegrambotservice.enity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
    Optional<SubscriptionEntity> findByTelegramUserId(Long telegramUserId);

    boolean existsByTelegramUserIdAndSubscribedTrue(Long telegramUserId);

    List<SubscriptionEntity> findAllBySubscribedTrue();

    long countBySubscribedTrue();
}
