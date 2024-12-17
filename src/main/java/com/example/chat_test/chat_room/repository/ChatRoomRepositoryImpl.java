package com.example.chat_test.chat_room.repository;

import com.example.chat_test.chat_room.ChatRoomType;
import com.example.chat_test.chat_room.entity.ChatRoom;
import com.example.chat_test.chat_room.service.ChatRoomRepository;
import com.example.chat_test.chat_user.entity.ChatUser;
import com.example.chat_test.chat_user.repository.ChatUserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChatRoomRepositoryImpl implements ChatRoomRepository {
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final ChatUserJpaRepository chatUserJpaRepository;


    @Override
    public Long createChatRoom(ChatRoomType type) {
        ChatRoom chatRoom = ChatRoom.builder()
                .type(type)
                .build();

        return chatRoomJpaRepository.save(chatRoom).getId();
    }


    @Override
    public ChatRoom getChatRoom(Long chatRoomId) {
        ChatRoom room = chatRoomJpaRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat Room Not Found"));

        return room;
    }

    @Override
    public List<ChatRoom> getChatRooms(Long userId) {
        return chatRoomJpaRepository.findChatRoomsByUserId(userId);
    }

    @Override
    public void deleteChatRoom(Long chatRoomId) {
        chatRoomJpaRepository.deleteById(chatRoomId);
    }


}
