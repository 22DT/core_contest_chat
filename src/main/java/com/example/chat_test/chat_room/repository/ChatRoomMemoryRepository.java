package com.example.chat_test.chat_room.repository;

import com.example.chat_test.chat_room.ChatRoomRepository;
import com.example.chat_test.chat_room.entity.ChatRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Slf4j
public class ChatRoomMemoryRepository implements ChatRoomRepository {
    private Long sequence = 0L;
    private Map<Long , ChatRoom> chatRooms = new ConcurrentHashMap<>();


    @Override
    public Long createChatRoom() {
        return null;
    }

    @Override
    public ChatRoom getChatRoom(Long chatRoomId) {
        return chatRooms.get(chatRoomId);
    }

    @Override
    public List<ChatRoom> getChatRooms() {
        return new ArrayList<>(chatRooms.values());
    }
}
