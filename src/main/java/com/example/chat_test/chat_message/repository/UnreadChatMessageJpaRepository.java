package com.example.chat_test.chat_message.repository;

import com.example.chat_test.chat_message.entity.UnreadChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnreadChatMessageJpaRepository extends JpaRepository<UnreadChatMessage, Long> {
}
