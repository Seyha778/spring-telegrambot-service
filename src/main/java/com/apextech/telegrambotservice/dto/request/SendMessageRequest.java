package com.apextech.telegrambotservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SendMessageRequest {
    @JsonProperty("chat_id")
    @NotBlank
    private String chatId;

    @JsonProperty("message")
    @NotBlank
    private String message;
}
