package com.example.chat_test.chat_room.service;

import com.example.chat_test.chat_room.ChatRoomType;
import com.example.chat_test.chat_room.entity.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository {

     Long createChatRoom(ChatRoomType type);

     ChatRoom getChatRoom(Long chatRoomId);
     ChatRoom getPrivateChatRoom(Long chatRoomId);
     List<ChatRoom> getChatRooms(Long userId);



     void deleteChatRoom(Long chatRoomId);


     boolean existsChatRoomByTeamId(Long teamId);


}
