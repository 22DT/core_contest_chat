package com.example.chat_test.chat_room.service;

import com.example.chat_test.chat_room.entity.ChatRoom;
import com.example.chat_test.user.service.UserService;
import com.example.chat_test.user.service.data.UserDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;


    public Long createGroupChatRoom(UserDomain user ,Long teamId) {
        // team 의 유저를 모두 찾아온다.
        List<UserDomain> teamUsers = userService.getUsersByTeam(teamId);


        // 채팅방 생성
        Long chatRoomId = chatRoomRepository.createGroupChatRoom();
        // 채팅 유저 생성.
        return null ;
    }


    public ChatRoom getChatRoom(Long chatRoomId) {
        return chatRoomRepository.getChatRoom(chatRoomId);
    }

    public List<ChatRoom> getChatRooms(){
        return chatRoomRepository.getChatRooms();
    }
}
