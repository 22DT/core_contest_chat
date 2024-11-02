package com.example.chat_test.chat_message.service;

import com.example.chat_test.chat_message.MessageType;
import com.example.chat_test.chat_message.entity.ChatMessage;
import com.example.chat_test.user.service.data.UserDomain;

public interface ChatMessageRepository {
    ChatMessage saveChatMessage(String message, MessageType type, UserDomain user, Long roomId);
}
