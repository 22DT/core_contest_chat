package com.example.chat_test.chat_room.service;

import com.example.chat_test.chat_room.ChatRoomType;
import com.example.chat_test.chat_room.entity.ChatRoom;

import java.util.List;

public interface ChatRoomRepository {

     Long createChatRoom(ChatRoomType type);


     ChatRoom getChatRoom(Long chatRoomId);

     List<ChatRoom> getChatRooms();
}
