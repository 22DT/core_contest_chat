package com.example.chat_test.chat;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
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
    private final SubscriptionRegistry subscriptionRegistry;


    @MessageMapping("/test")
//    @SendTo("topic/greetings")
    public void sendMessage(@Payload ChatMessage chatMessage, StompHeaderAccessor accessor) {
        log.info("[ChatController][sendMessage]");
        log.info("chatMessage: {}", chatMessage);
        Set<SimpUser> users = simpUserRegistry.getUsers();

        log.info("users= {}", users);

//        message.setContent("user"+message.getName()+": "+message.getContent());

        List<Long> onlineUsers = getOnlineUsers(chatMessage.getRoomId());
        log.info("onlineUsers= {}", onlineUsers);

        template.convertAndSend("/topic/" + chatMessage.roomId, chatMessage);
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

    @Data
    public static class ChatMessage{
        private Long roomId;
        private String name;
        private String content;
    }
}
