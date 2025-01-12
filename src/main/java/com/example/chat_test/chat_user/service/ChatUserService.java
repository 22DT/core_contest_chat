package com.example.chat_test.chat_user.service;

import com.example.chat_test.chat_user.dto.response.ChatUserResponse;
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


    /*public void createChatUsers(List<UserDomain> users, Long chatRoomId){
        chatUserRepository.saveChatUsers(users, chatRoomId);
    }*/

    public List<ChatUserResponse> getChatUsers(Long roomId){
        List<ChatUser> chatUsers = chatUserRepository.getChatUsers(roomId);

        return chatUsers.stream()
                .map((chatUser)->new ChatUserResponse(chatUser.getId(), chatUser.getUser().getId()))
                .toList();
    }


}
