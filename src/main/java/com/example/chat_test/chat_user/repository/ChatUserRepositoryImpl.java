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

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChatUserRepositoryImpl implements ChatUserRepository {
    private final UserJpaRepository userJpaRepository;
    private final ChatUserJpaRepository chatUserJpaRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;

    @Override
    public void saveCharUsers(List<UserDomain> users, Long chatRoomId) {
        List<ChatUser> chatUsers = new ArrayList<>();

        ChatRoom chatRoom = chatRoomJpaRepository.getReferenceById(chatRoomId);
        for (UserDomain user : users) {
            User proxyUser = userJpaRepository.getReferenceById(user.getId());

            ChatUser chatUser = ChatUser.builder()
                    .chatRoom(chatRoom)
                    .user(proxyUser)
                    .build();

            chatUsers.add(chatUser);

        }

        chatUserJpaRepository.saveAll(chatUsers);
    }
}
