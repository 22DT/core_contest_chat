package com.example.chat_test.exception.chat_room;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatRoomErrorCode {
    DUPLICATED_CHAT_ROOM(HttpStatus.CONFLICT, "User already joined this chat room"),
    CANNOT_LEAVE_CHAT_ROOM(HttpStatus.BAD_REQUEST, "Cannot leave a group chat room"),
    CANNOT_CREATE_SINGLE_CHAT_ROOM(HttpStatus.BAD_REQUEST, "Cannot create a chat room with only one user"),
    CANNOT_LEAVE_CHAT_ROOM_AGAIN(HttpStatus.BAD_REQUEST, "Cannot leave a chat room again after already leaving"),
    CANNOT_VIEW_LEFT_CHAT_ROOM(HttpStatus.BAD_REQUEST, "Cannot view a chat room after leaving it");

    private final HttpStatus status;
    private final String message;
}
