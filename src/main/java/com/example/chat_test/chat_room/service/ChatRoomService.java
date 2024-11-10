package com.example.chat_test.chat_room.service;

import com.example.chat_test.chat_message.MessageType;
import com.example.chat_test.chat_message.dto.response.ChatMessageResponse;
import com.example.chat_test.chat_message.entity.ChatMessage;
import com.example.chat_test.chat_message.service.ChatMessageRepository;
import com.example.chat_test.chat_message.service.ChatMessageUtil;
import com.example.chat_test.chat_message.service.db.ChatMessageReader;
import com.example.chat_test.chat_room.ChatRoomType;
import com.example.chat_test.chat_room.dto.response.ChatRoomPreviewResponse;
import com.example.chat_test.chat_room.dto.response.ChatRoomResponse;
import com.example.chat_test.chat_room.entity.ChatRoom;
import com.example.chat_test.chat_user.entity.ChatUser;
import com.example.chat_test.chat_user.service.ChatUserRepository;
import com.example.chat_test.user.service.UserService;
import com.example.chat_test.user.service.data.UserDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;
    private final ChatUserRepository chatUserRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageReader chatMessageReader;

    public Long createGroupChatRoom(UserDomain user ,Long teamId) {
        // team 의 유저를 모두 찾아온다. (팀장 포함?)
        List<UserDomain> teamUsers = userService.getUsersByTeam(teamId);

        // 채팅방 생성
        Long chatRoomId = chatRoomRepository.createChatRoom(ChatRoomType.GROUP);
        // 채팅 유저 생성.
        chatUserRepository.saveCharUsers(teamUsers,chatRoomId);
        chatMessageRepository.sendRoomCreationMessage(chatRoomId);

        return chatRoomId ;
    }

    public Long createPrivateChatRoom(UserDomain user ,Long targetUserId) {
        UserDomain targetUser = userService.getUserById(targetUserId);

        // 채팅방 생성
        Long chatRoomId = chatRoomRepository.createChatRoom(ChatRoomType.PRIVATE);
        // 채팅 유저 생성.
        chatUserRepository.saveCharUsers(List.of(user,targetUser),chatRoomId);
        chatMessageRepository.sendRoomCreationMessage(chatRoomId);

        return chatRoomId;
    }


    /**
     *
     * @param chatRoomId
     * @param user
     * @return
     *
     * 1) lastAccessTime 갱신만 하면 될 듯?
     *
     *
     */
    public ChatRoomResponse getChatRoom(Long chatRoomId, UserDomain user) {
        log.info("[ChatRoomService][getChatRoom]");

        // chatUser 찾아온다.
        List<ChatUser> chatUsers = chatUserRepository.getChatUsers(chatRoomId);
        ChatUser chatUser = null;

        for (ChatUser cu : chatUsers) {
            if(Objects.equals(cu.getUser().getId(), user.getId())){
                chatUser = cu;
                break;
            }
        }
        if(chatUser==null){
            throw new IllegalArgumentException("[ChatRoomService][getChatRoom][ChatUser is not found]");
        }

        // 마지막 접속 시간 갱신.
        LocalDateTime oldTime=chatUser.getLastAccessedAt();
        chatUser.updateLastAccessedAt();
        LocalDateTime newTime = chatUser.getLastAccessedAt();


        // 읽지 않은 메시지 읽음 처리.
        Integer maxReadCount=chatUsers.size();
        chatMessageRepository.incrementUnreadMessageCount(chatUser.getId(),chatRoomId, newTime, oldTime,  maxReadCount);

        // 메시지 갖고 온다.
        Slice<ChatMessage> chatMessages = chatMessageReader.getChatMessages(chatRoomId, user, 0, chatUser.getLastAccessedAt());

        Slice<ChatMessageResponse> chatMessageResponses = ChatMessageUtil.chatMessageToResponse(chatMessages);

        return new ChatRoomResponse(
                chatRoomId,
                "roomName",
                chatMessageResponses
        );

    }


    public List<ChatRoomPreviewResponse> getChatRooms(UserDomain user) {
        log.info("[ChatRoomService][getChatRooms]");

        // 채팅유저를 갖고 온다.
        List<ChatUser> chatUsers = chatUserRepository.getChatUsersByUserId(user.getId());
        Map<Long, ChatRoom> roomMap = new HashMap<>();
        for (ChatUser chatUser : chatUsers) {
            ChatRoom chatRoom = chatUser.getChatRoom();
            roomMap.put(chatRoom.getId(), chatRoom);
        }
        log.info("chatUsers.size()= {}", chatUsers.size());


        // 각 채팅방들의 최근 메시지를 갖고 온다.  이거 N+1 생기는 거 같은데...
        List<ChatMessage> mostRecentMessages = new ArrayList<>();
        for (ChatUser chatUser : chatUsers) {
            ChatRoom chatRoom = chatUser.getChatRoom();
            ChatMessage mostRecentMessage = chatMessageRepository.getMostRecentMessage(chatRoom.getId());

            mostRecentMessages.add(mostRecentMessage);
        }
        mostRecentMessages.sort(Comparator.comparing(ChatMessage::getCreatedAt).reversed());

        // key: roomId
        Map<Long, ChatUser> chatUserMap = new HashMap<>();
        for (ChatUser chatUser : chatUsers) {
            ChatRoom chatRoom = chatUser.getChatRoom();
            chatUserMap.put(chatRoom.getId(), chatUser);
        }

        List<ChatRoomPreviewResponse> responses = new ArrayList<>();
        for (ChatMessage mostRecentMessage : mostRecentMessages) {
            ChatRoom chatRoom = roomMap.get(mostRecentMessage.getChatRoom().getId());
            ChatUser chatUser = chatUserMap.get(chatRoom.getId());

            boolean isThereNewMessage = mostRecentMessage.getCreatedAt().isAfter(chatUser.getLastAccessedAt());
            if(mostRecentMessage.getMessageType()==MessageType.ENTER){isThereNewMessage=false;}

            ChatRoomPreviewResponse preview = new ChatRoomPreviewResponse(chatRoom.getId(), "roomName", mostRecentMessage.getMessage(), chatRoom.getType(), isThereNewMessage);
            responses.add(preview);
        }


        return responses;
    }
}
