package com.example.chat_test.chat_message.dto.request;

import com.example.chat_test.chat_message.MessageType;

public record ChatMessageRequest(
        Long userId,  // 이거는 jwt 와 SecurityHolder 로 할 거임.  삭제 예정임

        Long roomId,
        String message,
        MessageType messageType
) {
}
