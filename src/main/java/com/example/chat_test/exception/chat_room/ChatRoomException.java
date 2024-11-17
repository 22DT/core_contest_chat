package com.example.chat_test.exception.chat_room;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatRoomException extends RuntimeException {
    private final ChatRoomErrorCode errorCode;
}
