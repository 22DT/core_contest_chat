package com.example.chat_test.chat_message.dto.response;

import com.example.chat_test.chat_message.MessageType;
import com.example.chat_test.chat_message.entity.ChatMessage;

import java.time.LocalDateTime;

public record ChatMessageResponse(
        Long userId,
        String nickname,
        String snsProfileImageUrl,

        Long messageId,
        String message,
        MessageType messageType,

        Integer readCount,
        LocalDateTime createdAt
) {

    public static ChatMessageResponse from(ChatMessage message) {
        Long userId = (message.getMessageType() == MessageType.ENTER) ? null : message.getChatUser().getUser().getId();
        String nickname = (message.getMessageType() == MessageType.ENTER) ? null : message.getChatUser().getUser().getNickname();
        String snsProfileImageUrl = (message.getMessageType() == MessageType.ENTER) ? null : message.getChatUser().getUser().getSnsProfileImageUrl();

        Integer readCount = (message.getMessageType() == MessageType.ENTER) ? null : message.getReadCount();

        return new ChatMessageResponse(
                userId, nickname, snsProfileImageUrl, message.getId(), message.getMessage(), message.getMessageType(), readCount, message.getCreatedAt()
        );
    }
}
