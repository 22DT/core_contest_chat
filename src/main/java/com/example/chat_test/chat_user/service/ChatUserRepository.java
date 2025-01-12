package com.example.chat_test.chat_user.service;

import com.example.chat_test.chat_user.entity.ChatUser;
import com.example.chat_test.user.service.data.UserDomain;

import java.util.List;
import java.util.Optional;

public interface ChatUserRepository {
    void saveChatUsers(List<UserDomain> users, Long chatRoomId, Long startMessageId);

    List<ChatUser> getChatUsers(Long chatRoomId);
    List<ChatUser> getChatUsersByUserId(Long userId);
    ChatUser getChatUser(Long chatRoomId,Long userId);
    Optional<ChatUser> getChatUserById(Long chatRoomId, Long userId);
    List<ChatUser> getPrivateChatUser(Long userId);

    void deleteChatUser(Long chatRoomId,Long userId);
    void deleteChatUser(Long chatUserId);
    void deleteChatUsersByRoomId(Long roomId);

}
