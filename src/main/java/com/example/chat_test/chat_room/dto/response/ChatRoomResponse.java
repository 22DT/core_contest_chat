package com.example.chat_test.chat_room.dto.response;

import com.example.chat_test.chat_message.dto.response.ChatMessageResponse;
import org.springframework.data.domain.Slice;

public record ChatRoomResponse(
        Long roomId,
        String roomName,

        Slice<ChatMessageResponse> messages

) {
}
