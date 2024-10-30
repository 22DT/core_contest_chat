package com.example.chat_test.chat_message.api;

import com.example.chat_test.chat_message.dto.request.SendChatMessageRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.broker.SubscriptionRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController  {
    private final SimpMessageSendingOperations template;
    private final SimpUserRegistry simpUserRegistry;


    @MessageMapping("/topic")
//    @SendTo("topic/greetings")
    public void topic(@Payload SendChatMessageRequest chatMessage) {
        log.info("[ChatController][topic]");
        log.info("chatMessage: {}", chatMessage);
        Set<SimpUser> users = simpUserRegistry.getUsers();

        log.info("users= {}", users);

//        message.setContent("user"+message.getName()+": "+message.getContent());

        List<Long> onlineUsers = getOnlineUsers(chatMessage.roomId());
        log.info("onlineUsers= {}", onlineUsers);

        template.convertAndSend("/topic/" + chatMessage.roomId(), chatMessage);
    }

    @MessageMapping("/queue")
    public void queue(@Payload SendChatMessageRequest chatMessage) {
        log.info("[ChatController][queue]");
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
