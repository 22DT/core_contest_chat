package com.example.chat_test.chat_room.repository;

import com.example.chat_test.chat_room.entity.ChatRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select room from ChatRoom room" +
            " join fetch room.chatUsers cu" +
            " where cu.user.id=:userId")
    List<ChatRoom> findChatRoomsByUserId(@Param("userId")Long userId);


}
