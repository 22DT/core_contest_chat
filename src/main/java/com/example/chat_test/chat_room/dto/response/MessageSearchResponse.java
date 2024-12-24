package com.example.chat_test.chat_room.dto.response;

import com.example.chat_test.chat_message.dto.response.ChatMessageResponse;
import com.example.chat_test.chat_message.entity.ChatMessage;
import org.springframework.data.domain.Slice;

import java.util.List;

public record MessageSearchResponse(
        Slice<ChatMessageResponse> chatMessages,
        List<Long> messageIds
) {
}
