package com.example.chat_test.chat_room.repository;

import com.example.chat_test.chat_room.entity.NewMessageRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface NewChatMessageRoomJpaRepository extends JpaRepository<NewMessageRoom, Long> {

    @Modifying
    void deleteByChatRoomIdAndChatUserId(Long chatRoomId, Long chatUserId);

    boolean existsNewMessageRoomByChatRoomIdAndChatUserId(Long chatRoomId, Long chatUserId);

}
