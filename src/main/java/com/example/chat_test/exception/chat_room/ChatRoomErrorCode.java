package com.example.chat_test.exception.chat_room;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatRoomErrorCode {
    DUPLICATED_CHAT_ROOM(HttpStatus.CONFLICT, "User already joined this chat room");

    private final HttpStatus status;
    private final String message;
}
