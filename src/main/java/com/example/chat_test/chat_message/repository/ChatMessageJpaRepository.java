package com.example.chat_test.chat_message.repository;

import com.example.chat_test.chat_message.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessage, Long> {
    @Query("select message from ChatMessage message" +
            " left join message.chatUser cu" +
            " left join cu.user" +
            " where message.chatRoom.id=:roomId and message.id >=:startMessageId and message.id <= :lastMessageId")
    Slice<ChatMessage> findChatMessageByRoomId(@Param("roomId")Long roomId, @Param("startMessageId") Long startMessageId, @Param("lastMessageId") Long lastMessageId, Pageable pageable);



    @Query("select message from ChatMessage message" +
            " join fetch message.chatRoom" +
            " where message.chatRoom.id=:chatRoomId" +
            " order by message.id desc " +
            " limit  1")
    Optional<ChatMessage> findTopByChatRoomIdOrderByCreatedAtDesc(@Param("chatRoomId")Long chatRoomId);

    /*
    @Query("select message from ChatMessage message" +
            " join fetch message.chatRoom" +
            " where message.chatRoom.id in :chatRoomIds" +
            " order by message.id desc " +
            " limit  1")
    List<ChatMessage> findTopsByChatRoomIdOrderByCreatedAtDesc(@Param("chatRoomIds")List<Long> chatRoomIds);
    */



    @Query("select message from ChatMessage message" +
//            " join fetch message.chatRoom" +
            " where message.id in (select max(m1.id) from ChatMessage m1 where m1.chatRoom.id in :chatRoomIds group by m1.chatRoom)")
    List<ChatMessage> findTopsByChatRoomIdOrderByCreatedAtDesc(@Param("chatRoomIds")List<Long> chatRoomIds);




    @Query("select message from ChatMessage message" +
            " left join message.chatUser cu" +
            " left join cu.user" +
            " where message.chatRoom.id=:roomId and message.id>=:startMessageId" +
            " and message.messageType='IMAGE'" +
            " order by message.id asc ")
    List<ChatMessage> findImagesByRoomId(@Param("roomId")Long roomId, @Param("startMessageId") Long startMessageId);


    @Modifying
    @Query("update ChatMessage message set message.readCount=message.readCount+1" +
            " where message.chatRoom.id=:roomId" +
            " and message.chatUser.id != :chatUserId" +
            " and message.id<=:new" +
            " and message.id>:old" +
            " and message.readCount<:maxReadCount")
    void incrementUnreadMessageCount(@Param("roomId") Long roomId, @Param("chatUserId") Long chatUserId,
                                     @Param("old")Long oldLastReadMessageId,  @Param("new")Long newLastReadMessageId,
                                     @Param("maxReadCount") Integer maxReadCount);



    @Modifying
    @Query("delete from ChatMessage message" +
            " where message.chatRoom.id=:roomId")
    void deleteChatMessagesByRoomId(@Param("roomId") Long roomId);



}
