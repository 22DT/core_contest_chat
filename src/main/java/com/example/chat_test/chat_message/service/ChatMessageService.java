package com.example.chat_test.chat_message.service;

import com.example.chat_test.chat_message.MessageType;
import com.example.chat_test.chat_message.dto.request.ChatMessageRequest;
import com.example.chat_test.chat_message.dto.response.ChatMessageResponse;
import com.example.chat_test.chat_message.entity.ChatMessage;
import com.example.chat_test.chat_user.entity.ChatUser;
import com.example.chat_test.chat_user.service.ChatUserRepository;
import com.example.chat_test.user.service.data.UserDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChatMessageService {
    private final SimpUserRegistry simpUserRegistry;
    private final ChatMessageRepository chatMessageRepository;
    private final UnreadChatMessageRepository unreadChatMessageRepository;
    private final ChatUserRepository chatUserRepository;

    public ChatMessageResponse save(ChatMessageRequest chatMessageDto, UserDomain user) {
        log.info("[ChatMessageService][save]");
        // 채팅 메시지 DB에 저장.
        MessageType messageType = chatMessageDto.messageType();
        String message = chatMessageDto.message();
        Long roomId = chatMessageDto.roomId();

        ChatMessage chatMessage = chatMessageRepository.saveChatMessage(message, messageType, user, roomId);

        // 오프라인 유저를 찾는다.
        List<Long> offlineUserIds = getOfflineUsers(roomId);
        log.info("offlineUserIds= {}", offlineUserIds);

        // 읽지 않은 메시지 DB에 저장한다.
        unreadChatMessageRepository.save(offlineUserIds, chatMessage.getId(), roomId);


        return new ChatMessageResponse(user.getId(), roomId, message, chatMessage.getCreatedAt(), messageType);
    }

    private List<Long> getOfflineUsers(Long roomId){
        log.info("[ChatMessageService][getOfflineUsers]");
        List<ChatUser> chatUsers = chatUserRepository.getChatUsers(roomId);
        List<Long> onlineUserIds = getOnlineUsers(roomId);
        List<Long> offlineUserIds=chatUsers.stream().map(ChatUser::getId).collect(Collectors.toList());
        log.info("allUserIds= {}", offlineUserIds);
        log.info("onlineUserIds= {}", onlineUserIds);
        offlineUserIds.removeAll(onlineUserIds);

        return offlineUserIds;
    }

    private List<Long> getOnlineUsers(Long roomId){
        Set<SimpUser> users = simpUserRegistry.getUsers();
        List<Long> onlineUsers = new ArrayList<>();
        for (SimpUser user : users) {
            Set<SimpSession> sessions = user.getSessions();
            for (SimpSession session : sessions) {
                Set<SimpSubscription> subscriptions = session.getSubscriptions();
                for (SimpSubscription subscription : subscriptions) {
                    String destination = subscription.getDestination();
                    int idx = destination.lastIndexOf('/');
                    if (idx != -1) {
                        Long userRoomId= Long.valueOf(destination.substring(idx+1));
                        if(roomId.equals(userRoomId)){
                            onlineUsers.add(Long.valueOf(user.getName()));
                        }

                    }

                }
            }
        }

        return onlineUsers;
    }


}
