package com.example.chat_test.chat_user.repository;

import com.example.chat_test.chat_room.entity.ChatRoom;
import com.example.chat_test.chat_room.repository.ChatRoomJpaRepository;
import com.example.chat_test.chat_user.entity.ChatUser;
import com.example.chat_test.chat_user.service.ChatUserRepository;
import com.example.chat_test.user.entity.User;
import com.example.chat_test.user.repository.UserJpaRepository;
import com.example.chat_test.user.service.data.UserDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChatUserRepositoryImpl implements ChatUserRepository {
    private final UserJpaRepository userJpaRepository;
    private final ChatUserJpaRepository chatUserJpaRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;

    @Override
    public void saveChatUsers(List<UserDomain> users, Long chatRoomId, Long startMessageId) {
        List<ChatUser> chatUsers = new ArrayList<>();

        ChatRoom chatRoom = chatRoomJpaRepository.getReferenceById(chatRoomId);
        for (UserDomain user : users) {
            User proxyUser = userJpaRepository.getReferenceById(user.getId());

            ChatUser chatUser = ChatUser.builder()
                    .chatRoom(chatRoom)
                    .user(proxyUser)
                    .joinMessageId(startMessageId)
                    .lastReadMessageId(startMessageId)
                    .isLeave(false)
                    .build();

            chatUsers.add(chatUser);

        }

        chatUserJpaRepository.saveAll(chatUsers);
    }

    @Override
    public List<ChatUser> getChatUsers(Long chatRoomId) {
        return chatUserJpaRepository.findChatUsersByChatRoomId(chatRoomId);
    }

    @Override
    public List<ChatUser> getChatUsersByUserId(Long userId) {
        return chatUserJpaRepository.findChatUsersByUserId(userId);
    }


    @Override
    public ChatUser getChatUser(Long roomId, Long userId) {
        log.info("[ChatUserRepositoryImpl][getChatUser]");
        log.info("roomId: {}", roomId);
        log.info("userId: {}", userId);
        return chatUserJpaRepository.findChatUserByUserIdAndChatRoomId(userId, roomId)
                .orElseThrow(() -> new IllegalArgumentException("ChatUser is not found"));

    }

    @Override
    public Optional<ChatUser> getChatUserById(Long chatRoomId, Long userId) {
        return chatUserJpaRepository.findChatUserByUserIdAndChatRoomId(userId, chatRoomId);
    }

    @Override
    public List<ChatUser> getPrivateChatUser(Long userId) {
        return chatUserJpaRepository.findPrivateChatUserByUserId(userId);
    }

    @Override
    public void deleteChatUser(Long chatRoomId, Long userId) {
        chatUserJpaRepository.deleteChatUserByChatRoomAndUserId(chatRoomId, userId);
    }

    @Override
    public void deleteChatUser(Long chatUserId) {
        chatUserJpaRepository.deleteById(chatUserId);
    }

    @Override
    public void deleteChatUsersByRoomId(Long roomId) {
        chatUserJpaRepository.deleteChatUsersByChatRoomId(roomId);
    }


}
