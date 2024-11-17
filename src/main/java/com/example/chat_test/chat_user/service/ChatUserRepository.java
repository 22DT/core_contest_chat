package com.example.chat_test.chat_user.service;

import com.example.chat_test.chat_user.entity.ChatUser;
import com.example.chat_test.user.service.data.UserDomain;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ChatUserRepository {
    void saveCharUsers(List<UserDomain> users,Long chatRoomId);

    List<ChatUser> getChatUsers(Long chatRoomId);
    List<ChatUser> getChatUsersByUserId(Long userId);
    ChatUser getChatUser(Long chatRoomId,Long userId);

}
