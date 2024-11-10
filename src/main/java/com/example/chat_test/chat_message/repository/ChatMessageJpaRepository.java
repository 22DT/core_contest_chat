package com.example.chat_test.chat_message.repository;

import com.example.chat_test.chat_message.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessage, Long> {
    @Query("select message from ChatMessage message" +
            " left join message.chatUser cu" +
            " left join cu.user" +
            " where message.chatRoom.id=:roomId and message.createdAt <= :lastAccessedAt")
    Slice<ChatMessage> findChatMessageByRoomId(@Param("roomId")Long roomId, @Param("lastAccessedAt") LocalDateTime lastAccessedAt, Pageable pageable);



    @Query("select message from ChatMessage message" +
            " join fetch message.chatRoom" +
            " where message.chatRoom.id=:chatRoomId" +
            " order by message.createdAt desc " +
            " limit  1")
    Optional<ChatMessage> findTopByChatRoomIdOrderByCreatedAtDesc(@Param("chatRoomId")Long chatRoomId);


    @Modifying
    @Query("update ChatMessage message set message.readCount=message.readCount+1" +
            " where message.chatUser.id=:chatUserId" +
            " and message.chatRoom.id=:roomId" +
            " and message.createdAt<=:newTime" +
            " and message.createdAt>:oldTime" +
            " and message.readCount<:maxReadCount")
    void incrementUnreadMessageCount(@Param("chatUserId") Long chatUserId, @Param("roomId") Long roomId,
                                     @Param("newTime") LocalDateTime newTime, @Param("oldTime") LocalDateTime oldTime,
                                     @Param("maxReadCount") Integer maxReadCount);



}
