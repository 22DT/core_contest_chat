package com.example.chat_test.chat_user.repository;

import com.example.chat_test.chat_user.entity.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatUserJpaRepository extends JpaRepository<ChatUser, Long> {
}
