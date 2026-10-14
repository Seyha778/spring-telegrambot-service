package com.apextech.telegrambotservice.bot;

import com.apextech.telegrambotservice.dto.request.TelegramRequest;
import com.apextech.telegrambotservice.service.TelegramService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class TelegramBot {

    private final RestClient telegramClient;
    private final TelegramService telegramService;

    private long offset = 0;

    /**
     * Poll Telegram for new messages.
     */
    @Scheduled(fixedDelay = 1000)
    public void pollUpdates() {
        try {
            JsonNode response = telegramClient.get()
                    .uri("/getUpdates?offset={offset}&timeout=25",
                            offset)
                    .retrieve()
                    .body(JsonNode.class);

            if (response == null || !response.path("ok").asBoolean()) {
                log.warn("Telegram API returned an unsuccessful response");
                return;
            }

            for (JsonNode update : response.path("result")) {

                long updateId =
                        update.path("update_id").asLong();

                // Acknowledge this update on the next request.
                offset = Math.max(offset, updateId + 1);

                JsonNode message = update.path("message");

                if (message.isMissingNode()
                        || !message.hasNonNull("text")) {
                    continue;
                }

                handleMessage(message);
            }

        } catch (Exception e) {
            log.error("Failed to poll Telegram updates", e);
        }
    }

    /**
     * Process incoming Telegram messages.
     */
    private void handleMessage(JsonNode message) {

        long chatId = message.path("chat")
                .path("id").asLong();

        JsonNode from = message.path("from");

        if (from.path("id").isMissingNode()) {
            return;
        }

        long telegramUserId = from.path("id").asLong();

        String username = from.hasNonNull("username")
                ? from.path("username").asText()
                : null;

        String firstName = from.hasNonNull("first_name")
                ? from.path("first_name").asText()
                : null;

        String text = message.path("text")
                .asText("")
                .trim();

        if (text.isBlank()) {
            return;
        }

        String command = text.split("\\s+", 2)[0]
                .split("@", 2)[0]
                .toLowerCase();

        String reply;

        try {
            reply = switch (command) {

                case "/start" ->
                        "Welcome to our Telegram bot!\n\n"
                                + "Available commands:\n"
                                + "/subscribe - Subscribe\n"
                                + "/unsubscribe - Unsubscribe\n"
                                + "/status - Check subscription\n"
                                + "/help - Show help";

                case "/help" ->
                        "Available commands:\n\n"
                                + "/subscribe - Subscribe to updates\n"
                                + "/unsubscribe - Stop receiving updates\n"
                                + "/status - Check your subscription status";

                case "/subscribe" -> {
                    TelegramRequest request = TelegramRequest.builder()
                            .telegramUserId(telegramUserId)
                            .chatId(chatId)
                            .username(username)
                            .firstName(firstName)
                            .build();
                    yield telegramService.subscribe(request);
                }

                case "/unsubscribe" ->
                        telegramService.unsubscribe(telegramUserId);

                case "/status" ->
                        telegramService.isSubscribed(telegramUserId)
                                ? "You are currently subscribed."
                                : "You are not subscribed.";

                default ->
                        "Unknown command. Send /help to see available commands.";
            };

        } catch (Exception e) {
            log.error(
                    "Failed to process command {} for Telegram user {}",
                    command,
                    telegramUserId,
                    e
            );

            reply = "Sorry, something went wrong. Please try again later.";
        }

        sendMessage(chatId, reply);
    }

    /**
     * Send a reply to a Telegram chat.
     */
    private void sendMessage(long chatId, String text) {
        try {
            telegramClient.post()
                    .uri("/sendMessage")
                    .body(Map.of(
                            "chat_id", chatId,
                            "text", text
                    ))
                    .retrieve()
                    .toBodilessEntity();

        } catch (Exception e) {
            log.error(
                    "Failed to send Telegram message to chat {}",
                    chatId,
                    e
            );
        }
    }
}
