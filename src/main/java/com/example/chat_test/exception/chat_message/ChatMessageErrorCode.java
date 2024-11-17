package com.example.chat_test.exception.chat_message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatMessageErrorCode {
    CANNOT_SEND_MESSAGE(HttpStatus.BAD_REQUEST, "메시지를 전송할 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
