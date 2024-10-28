package com.example.chat_test.chat_room.repository;

import com.example.chat_test.chat_room.ChatRoomType;
import com.example.chat_test.chat_room.entity.ChatRoom;
import com.example.chat_test.chat_room.service.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChatRoomJpaRepositoryImpl implements ChatRoomRepository {
    private final ChatRoomJpaRepository chatRoomJpaRepository;


    @Override
    public Long createGroupChatRoom() {
        ChatRoom chatRoom = ChatRoom.builder()
                .type(ChatRoomType.GROUP)
                .build();

        return chatRoomJpaRepository.save(chatRoom).getId();
    }

    @Override
    public ChatRoom getChatRoom(Long chatRoomId) {
        return null;
    }

    @Override
    public List<ChatRoom> getChatRooms() {
        return List.of();
    }
}
