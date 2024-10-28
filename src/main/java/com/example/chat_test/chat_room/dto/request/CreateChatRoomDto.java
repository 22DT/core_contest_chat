package com.example.chat_test.chat_room.dto.request;

import com.example.chat_test.chat_room.ChatRoomType;

import java.util.List;

public record CreateChatRoomDto(
        ChatRoomType type,
        List<Long> chatUserIds
) {
}
