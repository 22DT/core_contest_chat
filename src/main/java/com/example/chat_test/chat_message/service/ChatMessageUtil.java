package com.example.chat_test.chat_message.service;

import com.example.chat_test.chat_message.dto.response.ChatMessageResponse;
import com.example.chat_test.chat_message.entity.ChatMessage;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatMessageUtil {

    public static Slice<ChatMessageResponse> chatMessageToResponse(Slice<ChatMessage> messages) {
        return messages.map(ChatMessageResponse::from);

    }

    public static List<ChatMessageResponse> chatMessageToResponse(List<ChatMessage> messages) {
       return messages.stream()
               .map(ChatMessageResponse::from)
               .toList();


    }


}
