package com.apextech.telegrambotservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelegramRequest {

    @JsonProperty("telegram_user_id")
    private Long telegramUserId;

    @JsonProperty("chat_id")
    private Long chatId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("first_name")
    private String firstName;

}
