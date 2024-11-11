package com.example.chat_test.chat_message.repository;

import com.example.chat_test.chat_message.MessageType;
import com.example.chat_test.chat_message.entity.ChatMessage;
import com.example.chat_test.chat_message.service.ChatMessageRepository;
import com.example.chat_test.chat_room.entity.ChatRoom;
import com.example.chat_test.chat_room.repository.ChatRoomJpaRepository;
import com.example.chat_test.chat_user.entity.ChatUser;
import com.example.chat_test.chat_user.repository.ChatUserJpaRepository;
import com.example.chat_test.chat_user.service.ChatUserRepository;
import com.example.chat_test.user.service.data.UserDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChatMessageRepositoryImpl implements ChatMessageRepository {
    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final ChatUserRepository charUserRepository;
    private final ChatUserJpaRepository chatUserJpaRepository;


    @Override
    public ChatMessage saveChatMessage(String message, MessageType type, UserDomain user, Long roomId, Integer readCount) {
        ChatRoom chatRoom = chatRoomJpaRepository.getReferenceById(roomId);
        ChatUser chatUser = charUserRepository.getChatUser(roomId, user.getId());  //


        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .chatUser(chatUser)
                .message(message)
                .createdAt(LocalDateTime.now())
                .readCount(readCount)
                .messageType(type)
                .build();

        chatMessageJpaRepository.save(chatMessage);

        return chatMessage;
    }

    @Override
    public void sendRoomCreationMessage(Long roomId) {
        ChatRoom chatRoom = chatRoomJpaRepository.getReferenceById(roomId);

        ChatMessage chatMessage = ChatMessage.builder()
                .message("채팅방 생성!")
                .chatRoom((chatRoom))
                .createdAt(LocalDateTime.now())
                .messageType(MessageType.ENTER)
                .build();

        chatMessageJpaRepository.save(chatMessage);
    }

    @Override
    public Slice<ChatMessage> getChatMessages(Long roomId, LocalDateTime lastAccessedAt, Pageable pageable) {
        return chatMessageJpaRepository.findChatMessageByRoomId(roomId, lastAccessedAt, pageable);
    }

    @Override
    public ChatMessage getMostRecentMessage(Long roomId) {
        Optional<ChatMessage> message = chatMessageJpaRepository.findTopByChatRoomIdOrderByCreatedAtDesc(roomId);
        return message.orElseThrow(()->new IllegalArgumentException("message is not found"));

    }



    @Override
    public void incrementUnreadMessageCount(Long roomId, Long chatUserId,LocalDateTime newTime,  LocalDateTime oldTime, Integer maxReadCount) {
        log.info("[saveChatMessage][incrementUnreadMessageCount]");
        log.info("[incrementUnreadMessageCount][before]");
        chatMessageJpaRepository.incrementUnreadMessageCount(roomId, chatUserId, newTime, oldTime, maxReadCount);
        log.info("[incrementUnreadMessageCount][after]");
    }


}
