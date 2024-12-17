package com.example.chat_test.exception.chat_user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatUserErrorCode {
    USER_NOT_PARTICIPANT(HttpStatus.NOT_FOUND, "채팅방의 참가자가 아님.");


    private final HttpStatus status;
    private final String message;
}
