package com.example.chat_test.chat_message.dto.response;

import com.example.chat_test.chat_message.MessageType;

import java.time.LocalDateTime;

public record ChatMessageResponse(
        Long userId,
        Long roomId,
        String message,
        LocalDateTime createdAt,
        MessageType messageType
) {
}
