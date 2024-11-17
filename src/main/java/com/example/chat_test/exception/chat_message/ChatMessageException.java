package com.example.chat_test.exception.chat_message;

import com.example.chat_test.chat_message.entity.ChatMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatMessageException extends RuntimeException {
    private final ChatMessageErrorCode errorCode;
}
