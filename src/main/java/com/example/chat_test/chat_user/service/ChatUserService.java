package com.example.chat_test.chat_user.service;

import com.example.chat_test.chat_user.entity.ChatUser;
import com.example.chat_test.user.service.data.UserDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatUserService {
    private final ChatUserRepository chatUserRepository;


    public void createChatUsers(List<UserDomain> users, Long chatRoomId){
        chatUserRepository.saveCharUsers(users, chatRoomId);
    }

    public List<ChatUser> getChatUsers(Long roomId){
        return chatUserRepository.getChatUsers(roomId);
    }


}
