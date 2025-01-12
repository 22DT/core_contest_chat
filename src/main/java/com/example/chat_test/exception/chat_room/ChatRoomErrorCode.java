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
    CANNOT_VIEW_LEFT_CHAT_ROOM(HttpStatus.BAD_REQUEST, "Cannot view a chat room after leaving it"),
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "The chat room could not be found"),
    CANNOT_CREATE_DUPLICATE_CHAT_ROOM(HttpStatus.CONFLICT, "Cannot create a duplicate chat room"),
    CANNOT_JOIN_PRIVATE_CHAT_ROOM(HttpStatus.BAD_REQUEST, "Cannot join a private chat room"),
    ALREADY_JOINED_CHAT_ROOM(HttpStatus.CONFLICT, "User is already a member of the chat room");


    private final HttpStatus status;
    private final String message;
}
