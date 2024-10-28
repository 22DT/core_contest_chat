package com.example.chat_test.chat_room.repository;

import com.example.chat_test.chat_room.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long> {
}
