package com.example.chat_test.chat_user.repository;

import com.example.chat_test.chat_user.entity.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatUserJpaRepository extends JpaRepository<ChatUser, Long> {

    @Query("select cu from ChatUser cu" +
            " where cu.chatRoom.id=:roomId")
    List<ChatUser> findChatUsersByRoomId(@Param("roomId") Long roomId);


    @Query("select cu from ChatUser cu" +
            " where cu.user.id=:userId and cu.chatRoom.id=:roomId")
    Optional<ChatUser> findChatUserByUserIdAndChatRoomId(@Param("userId")Long userId, @Param("roomId")Long roomId);
}
