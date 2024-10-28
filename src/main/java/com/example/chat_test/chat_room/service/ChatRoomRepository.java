package com.example.chat_test.chat_room.service;

import com.example.chat_test.chat_room.entity.ChatRoom;

import java.util.List;

public interface ChatRoomRepository {

     Long createGroupChatRoom();


     ChatRoom getChatRoom(Long chatRoomId);

     List<ChatRoom> getChatRooms();
}
