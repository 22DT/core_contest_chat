package com.example.chat_test.chat_message.repository;

import com.example.chat_test.chat_message.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessage, Long> {
}
