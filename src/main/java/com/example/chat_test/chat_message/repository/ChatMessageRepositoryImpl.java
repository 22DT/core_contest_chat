package com.example.chat_test.chat_message.repository;

import com.example.chat_test.chat_message.MessageType;
import com.example.chat_test.chat_message.entity.ChatMessage;
import com.example.chat_test.chat_message.service.ChatMessageRepository;
import com.example.chat_test.chat_room.entity.ChatRoom;
import com.example.chat_test.chat_room.repository.ChatRoomJpaRepository;
import com.example.chat_test.chat_user.entity.ChatUser;
import com.example.chat_test.chat_user.service.ChatUserRepository;
import com.example.chat_test.user.service.data.UserDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChatMessageRepositoryImpl implements ChatMessageRepository {
    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final ChatUserRepository charUserRepository;


    @Override
    public ChatMessage saveChatMessage(String message, MessageType type, UserDomain user, Long roomId) {
        ChatRoom chatRoom = chatRoomJpaRepository.getReferenceById(roomId);
        ChatUser chatUser = charUserRepository.getChatUser(roomId, user.getId());  //


        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .Writer(chatUser)
                .message(message)
                .createdAt(LocalDateTime.now())
                .messageType(type)
                .build();

        chatMessageJpaRepository.save(chatMessage);

        return chatMessage;
    }




}
