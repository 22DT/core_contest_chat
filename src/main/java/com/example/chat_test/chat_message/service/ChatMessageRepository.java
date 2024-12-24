package com.example.chat_test.chat_message.service;

import com.example.chat_test.chat_message.MessageType;
import com.example.chat_test.chat_message.entity.ChatMessage;
import com.example.chat_test.user.service.data.UserDomain;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository {
    ChatMessage saveChatMessage(String message, MessageType type, UserDomain user, Long roomId, Integer readCount);
    void sendRoomCreationMessage(Long roomId);

    Slice<ChatMessage> getChatMessages(Long roomId, LocalDateTime lastJoinedAt, LocalDateTime lastAccessedAt, Pageable pageable);
    ChatMessage getMostRecentMessage(Long roomId);
    List<ChatMessage> getMostRecentMessages(List<Long> roomIds);
    List<ChatMessage> getImages(Long roomId, LocalDateTime lastJoinedAt);

    void incrementUnreadMessageCount(Long roomId, Long chatUserId,LocalDateTime newTime,  LocalDateTime oldTime, Integer maxReadCount);

    void deleteMessagesByRoomId(Long roomId);
}
