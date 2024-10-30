package com.example.chat_test.chat_message.dto.request;

import com.example.chat_test.chat_message.MessageType;

import java.time.LocalDateTime;

public record SendChatMessageRequest(
        Long userId,  // 이거는 jwt 와 SecurityHolder 로 할 거임.

        Long roomId,
        String message,
        LocalDateTime createdAt,
        MessageType messageType
) {
}
