package com.example.chat_test.exception.chat_user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatUserException extends RuntimeException {
    private final ChatUserErrorCode errorCode;
}
