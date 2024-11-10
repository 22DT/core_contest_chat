package com.example.chat_test.chat_room.dto.response;

import com.example.chat_test.chat_room.ChatRoomType;

public record ChatRoomPreviewResponse(
        Long roomId,
        String roomName,
        String content,
        ChatRoomType chatRoomType,
        boolean isThereNewMessage
) {
}
