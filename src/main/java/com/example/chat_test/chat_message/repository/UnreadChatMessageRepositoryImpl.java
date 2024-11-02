package com.example.chat_test.chat_message.repository;

import com.example.chat_test.chat_message.entity.ChatMessage;
import com.example.chat_test.chat_message.entity.UnreadChatMessage;
import com.example.chat_test.chat_message.service.UnreadChatMessageRepository;
import com.example.chat_test.chat_room.entity.ChatRoom;
import com.example.chat_test.chat_room.repository.ChatRoomJpaRepository;
import com.example.chat_test.chat_user.entity.ChatUser;
import com.example.chat_test.chat_user.repository.ChatUserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UnreadChatMessageRepositoryImpl implements UnreadChatMessageRepository {
    private final UnreadChatMessageJpaRepository unreadChatMessageJpaRepository;
    private final ChatUserJpaRepository chatUserJpaRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;


    @Override
    public void save(List<Long> offlineUserIds, Long chatMessageId, Long chatRoomId) {
        log.info("[UnreadChatMessageRepositoryImpl][save]");

        ChatRoom chatRoom = chatRoomJpaRepository.getReferenceById(chatRoomId);
        ChatMessage chatMessage = chatMessageJpaRepository.getReferenceById(chatMessageId);
        List<UnreadChatMessage> unreadChatMessages=new ArrayList<>();

        for (Long offlineUserId : offlineUserIds) {
            ChatUser chatUser = chatUserJpaRepository.getReferenceById(offlineUserId);

            UnreadChatMessage unreadChatMessage = UnreadChatMessage.builder()
                    .chatUser(chatUser)
                    .chatRoom(chatRoom)
                    .chatMessage(chatMessage)
                    .build();

            unreadChatMessages.add(unreadChatMessage);
        }

        unreadChatMessageJpaRepository.saveAll(unreadChatMessages);
    }
}
