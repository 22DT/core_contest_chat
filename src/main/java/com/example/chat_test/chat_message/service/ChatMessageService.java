package com.example.chat_test.chat_message.service;

import com.example.chat_test.chat_message.MessageType;
import com.example.chat_test.chat_message.dto.request.ChatMessageRequest;
import com.example.chat_test.chat_message.dto.response.ChatMessageResponse;
import com.example.chat_test.chat_message.entity.ChatMessage;
import com.example.chat_test.chat_message.service.db.ChatMessageReader;
import com.example.chat_test.chat_room.service.ChatRoomService;
import com.example.chat_test.chat_user.ChatUserSession;
import com.example.chat_test.chat_user.entity.ChatUser;
import com.example.chat_test.chat_user.service.ChatUserRepository;
import com.example.chat_test.exception.chat_message.ChatMessageErrorCode;
import com.example.chat_test.exception.chat_message.ChatMessageException;
import com.example.chat_test.exception.chat_user.ChatUserErrorCode;
import com.example.chat_test.exception.chat_user.ChatUserException;
import com.example.chat_test.user.service.UserRepository;
import com.example.chat_test.user.service.data.UserDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatUserRepository chatUserRepository;
    private final ChatMessageReader chatMessageReader;



    public ChatMessageResponse send(ChatMessageRequest chatMessageDto, UserDomain user) {
        log.info("[ChatMessageService][send]");
        Long roomId=chatMessageDto.roomId();

        Long activeChatUserCount = ChatUserSession.getUserCount(roomId);
        int i = activeChatUserCount.intValue();
        boolean active = ChatUserSession.isActive(user.getId(), roomId);

        // 나간 유저인지 체크해줘야 하나?
        ChatUser chatUser = chatUserRepository.getChatUser(roomId, user.getId());

        if(chatUser.isLeave()){
            log.info("채팅방 참가자가 아님.");
            throw new ChatUserException(ChatUserErrorCode.USER_NOT_PARTICIPANT);
        }

        if(!active){
            log.info("user not active");
            throw new ChatMessageException(ChatMessageErrorCode.CANNOT_SEND_MESSAGE);
        }
        // 채팅 메시지 DB에 저장.
        MessageType messageType = chatMessageDto.messageType();
        String message = chatMessageDto.message();

        ChatMessage chatMessage = chatMessageRepository.saveChatMessage(message, messageType, user, roomId, i);


        return new ChatMessageResponse(user.getId(),user.getNickname(), user.getSnsProfileImageUrl(), null, message, messageType, chatMessage.getReadCount(), chatMessage.getCreatedAt());
    }

    /**
     * 특정 채팅방의 메시지들 조회.
     * @param roomId
     * @param page
     * @return
     *
     * @apiNote
     *
     * 채팅유저랑 유저 검색해서 이름 처리해줘야 함.
     *
     * 슬라이드 올릴 때 메시지 계속 발생하면??
     * 오래 전  -> 최근: 오래 전이 0부터 시작하게 페이징해야 함.
     * 날짜 오름차순 페이징해야 할 듯한데?
     *
     * 읽은 메시지 처리도 필요함.
     * 프론트에서 페이징 정보를 어떻게 알지?
     *
     * 이거 뒤집어서 응답해야 하나?
     */
    public Slice<ChatMessageResponse> getChatMessages(Long roomId, UserDomain user, Integer page, LocalDateTime lastAccessedAt){
        log.info("[ChatMessageService][getChatMessages]");
        Slice<ChatMessage> chatMessages = chatMessageReader.getChatMessages(roomId, user, page, lastAccessedAt);

        return ChatMessageUtil.chatMessageToResponse(chatMessages);
    }

    public List<ChatMessageResponse> getImages(Long roomId, UserDomain user){
        List<ChatMessage> images = chatMessageReader.getImages(roomId,user);

        return ChatMessageUtil.chatMessageToResponse(images);
    }


    private List<Long> getOfflineUsers(List<Long> onlineUserIds, Long roomId){
        log.info("[ChatMessageService][getOfflineUsers]");
        List<ChatUser> chatUsers = chatUserRepository.getChatUsers(roomId);
        List<Long> offlineUserIds=chatUsers.stream().map(ChatUser::getId).collect(Collectors.toList());
        log.info("allUserIds= {}", offlineUserIds);
        log.info("onlineUserIds= {}", onlineUserIds);
        offlineUserIds.removeAll(onlineUserIds);

        return offlineUserIds;
    }


}
