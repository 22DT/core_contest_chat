package com.example.chat_test.chat_room.service;

import com.example.chat_test.chat_room.ChatRoomType;
import com.example.chat_test.chat_room.entity.ChatRoom;

import java.util.List;

public interface ChatRoomRepository {

     Long createChatRoom(ChatRoomType type);
     void createNewMessageRoom(List<Long> chatUserIds, Long roomId);

     ChatRoom getChatRoom(Long chatRoomId);
     List<ChatRoom> getChatRooms(Long userId);

     void deleteNewMessageRoom(Long chatRoomId, Long chatUserId);


     boolean existsNewMessageChatRoom(Long chatRoomId, Long chatUserId);
}
