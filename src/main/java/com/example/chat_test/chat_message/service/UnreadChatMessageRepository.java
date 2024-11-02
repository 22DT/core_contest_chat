package com.example.chat_test.chat_message.service;

import java.util.List;

public interface UnreadChatMessageRepository {
     void save(List<Long> offlineUserIds, Long chatMessageId, Long chatRoomId);
}
