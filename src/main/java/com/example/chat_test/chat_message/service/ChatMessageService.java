package com.example.chat_test.chat_message.service;

import com.example.chat_test.chat_message.MessageType;
import com.example.chat_test.chat_message.dto.request.ChatMessageRequest;
import com.example.chat_test.chat_message.dto.response.ChatMessageResponse;
import com.example.chat_test.chat_message.entity.ChatMessage;
import com.example.chat_test.chat_message.service.db.ChatMessageReader;
import com.example.chat_test.chat_room.service.ChatRoomService;
import com.example.chat_test.chat_user.entity.ChatUser;
import com.example.chat_test.chat_user.service.ChatUserRepository;
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


    public ChatMessageResponse send(List<Long> onlineChatUserIds, ChatMessageRequest chatMessageDto, UserDomain user) {
        Long roomId=chatMessageDto.roomId();

        // 오프라인 유저를 찾는다.
        List<Long> offlineUserIds = getOfflineUsers(onlineChatUserIds, roomId);
        log.info("offlineUserIds= {}", offlineUserIds);

        log.info("[ChatMessageService][save]");
        // 채팅 메시지 DB에 저장.
        MessageType messageType = chatMessageDto.messageType();
        String message = chatMessageDto.message();

        ChatMessage chatMessage = chatMessageRepository.saveChatMessage(message, messageType, user, roomId, onlineChatUserIds.size());





        return new ChatMessageResponse(user.getId(),user.getNickname(), user.getSnsProfileImageUrl(), message, messageType, -1, chatMessage.getCreatedAt());
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
     */
    public Slice<ChatMessageResponse> getChatMessages(Long roomId, UserDomain user, Integer page, LocalDateTime lastAccessedAt){
        log.info("[ChatMessageService][getChatMessages]");
        Slice<ChatMessage> chatMessages = chatMessageReader.getChatMessages(roomId, user, page, lastAccessedAt);

        return ChatMessageUtil.chatMessageToResponse(chatMessages);
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
