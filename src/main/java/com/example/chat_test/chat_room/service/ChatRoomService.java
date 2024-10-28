package com.example.chat_test.chat_room.service;

import com.example.chat_test.chat_room.ChatRoomType;
import com.example.chat_test.chat_room.entity.ChatRoom;
import com.example.chat_test.chat_user.service.ChatUserRepository;
import com.example.chat_test.chat_user.service.ChatUserService;
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
    private final ChatUserService chatUserService;
    private final UserService userService;


    public Long createGroupChatRoom(UserDomain user ,Long teamId) {
        // team 의 유저를 모두 찾아온다. (팀장 포함?)
        List<UserDomain> teamUsers = userService.getUsersByTeam(teamId);

        // 채팅방 생성
        Long chatRoomId = chatRoomRepository.createChatRoom(ChatRoomType.GROUP);
        // 채팅 유저 생성.
        chatUserService.createChatUsers(teamUsers,chatRoomId);

        return chatRoomId ;
    }

    public Long createPrivateChatRoom(UserDomain user ,Long targetUserId) {
        UserDomain targetUser = userService.getUserById(targetUserId);

        // 채팅방 생성
        Long chatRoomId = chatRoomRepository.createChatRoom(ChatRoomType.PRIVATE);
        // 채팅 유저 생성.
        chatUserService.createChatUsers(List.of(targetUser),chatRoomId);


        return chatRoomId;
    }


    public ChatRoom getChatRoom(Long chatRoomId) {
        return chatRoomRepository.getChatRoom(chatRoomId);
    }

    public List<ChatRoom> getChatRooms(){
        return chatRoomRepository.getChatRooms();
    }
}
