package com.example.chat_test.chat_user.service;

import com.example.chat_test.chat_user.entity.ChatUser;
import com.example.chat_test.user.service.data.UserDomain;

import java.util.List;

public interface ChatUserRepository {
    void saveCharUsers(List<UserDomain> users,Long chatRoomId);

    List<ChatUser> getChatUsers(Long chatRoomId);
    ChatUser getChatUser(Long chatRoomId,Long userId);
}
