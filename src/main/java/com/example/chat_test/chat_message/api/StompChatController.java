package com.example.chat_test.chat_message.api;

import com.example.chat_test.chat_message.dto.request.ChatMessageRequest;
import com.example.chat_test.chat_message.dto.response.ChatMessageResponse;
import com.example.chat_test.chat_message.service.ChatMessageService;
import com.example.chat_test.chat_user.entity.ChatUser;
import com.example.chat_test.user.service.data.UserDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class StompChatController  {
    private final SimpMessageSendingOperations template;
    private final ChatMessageService chatMessageService;
    private final SimpUserRegistry simpUserRegistry;  // 그러고 보니 이거 같은 인스턴스 이용하는 건가?

    @MessageMapping("/topic")
//    @SendTo("topic/greetings")
    public void topic(@Payload ChatMessageRequest chatMessage) {
        log.info("[ChatController][topic]");
        log.info("chatMessage: {}", chatMessage);

        // 임시로
        UserDomain user = UserDomain.builder()
                .id(chatMessage.userId())
                .build();

        List<Long> onlineUserIds = getOnlineUsers(chatMessage.roomId());

        ChatMessageResponse chatMessageResponse = chatMessageService.send(chatMessage, user);

        template.convertAndSend("/topic/" + chatMessage.roomId(), chatMessageResponse);
    }

    @MessageMapping("/queue")
    public void queue(@Payload ChatMessageRequest chatMessage) {
        log.info("[ChatController][queue]");

        // 임시로
        UserDomain user = UserDomain.builder()
                .id(chatMessage.userId())
                .build();

    }


    /**
     *
     *
     * @apiNote
     * /app/sub 해야 호출됨
     */
    @SubscribeMapping("/sub")
    public void subscribe() {
        log.info("[ChatController][subscribe]");
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
