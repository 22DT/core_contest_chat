package com.example.chat_test.chat_user.repository;

import com.example.chat_test.chat_user.entity.ChatUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatUserJpaRepository extends JpaRepository<ChatUser, Long> {

    @Query("select cu from ChatUser cu" +
            " where cu.chatRoom.id=:roomId")
    List<ChatUser> findChatUsersByRoomId(@Param("roomId") Long roomId);


    @Query("select cu from ChatUser cu" +
            " join fetch cu.chatRoom" +
            " where cu.user.id=:userId and cu.chatRoom.id=:roomId")
    Optional<ChatUser> findChatUserByUserIdAndChatRoomId(@Param("userId")Long userId, @Param("roomId")Long roomId);

    @Query("select  cu from ChatUser cu" +
            " join fetch cu.user" +
            " where cu.chatRoom.id=:roomId")
    List<ChatUser> findChatUsersByChatRoomId(@Param("roomId")Long roomId);


    @Query("select cu from ChatUser cu" +
            " join fetch cu.chatRoom" +
            " where cu.user.id=:userId")
    List<ChatUser> findChatUsersByUserId(@Param("userId")Long userId);


    @Query("select cu from ChatUser cu" +
            " join fetch cu.chatRoom room" +
            " where room.type='PRIVATE' and cu.user.id=:userId")
    List<ChatUser> findPrivateChatUserByUserId(@Param("userId") Long userId);


    @Modifying
    @Query("delete from ChatUser cu" +
            " where cu.chatRoom.id=:roomId and cu.user.id=:userId")
    void deleteChatUserByChatRoomAndUserId(@Param("roomId")Long roomId, @Param("userId")Long userId);


    @Modifying
    @Query("delete from ChatUser cu" +
            " where cu.chatRoom.id=:roomId")
    void deleteChatUsersByChatRoomId(@Param("roomId")Long roomId);


}
