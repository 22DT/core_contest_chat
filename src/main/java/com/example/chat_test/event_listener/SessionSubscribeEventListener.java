package com.example.chat_test.event_listener;

import com.example.chat_test.chat_message.MessageType;
import com.example.chat_test.chat_message.dto.request.ChatMessageRequest;
import com.example.chat_test.chat_message.dto.response.ChatMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.broker.SubscriptionRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.DefaultSimpUserRegistry;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SessionSubscribeEventListener {
    private final SimpMessageSendingOperations template;
    private final SimpUserRegistry simpUserRegistry;
    private final SubscriptionRegistry subscriptionRegistry;


    /**
     *
     * @param event
     *
     * @apiNote
     *
     * DefaultSimpUserRegistry.onSessionSubscribe() 호출 후에 이게 호출되네 뭐야?
     * 위에서 중복으로 구독이 추가됨. 이거 내가 지워줘야 할 것 같은데?
     * event 에 user 안 들어 있음. 근데 세션 아이디는 있네 이럼 상관없을 거 같은데?
     * 세션 아이디로 세션 객체 갖고 와야 함 구독 정보 set 으로 리턴해서 안 됨.
     * findSubscriptions() 이걸로 해당 세션 아이디의 구독 목혹을 갖고와.
     * 그리고 현재 목적이랑 같은 구독이 있으면 그냥 아무 것도 안 하는 걸로 하자.
     * 
     * 1) destination에서 채팅방 번호 갖고 온다 -> 유효한 건지 확인.
     * 2) 중복 구독인지 확인
     *
     * 토큰에 있는 username( id로 하자)로 user 갖고 옴.
     * 갖고 온 SimpUser에서 세션 아디디로 세션 갖고 옴.
     * 갖고 온 세션에서 구독 목록 조회
     * event 의 dest 와 세션의 destination 중복 확인
     *
     * SimpMessageHeaderAccessor 여기 기능이 많네?
     *
     * 구독할 때 딱히 할 작업은 없는 거 같은데..?
     *
     */
    @EventListener
    public void onSessionSubscribe(SessionSubscribeEvent event) {
        log.info("[SessionSubscribeEventListener][onSessionSubscribe]");
        Message<byte[]> message = event.getMessage();
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String destination = accessor.getDestination();
        String sessionId = accessor.getSessionId();
        String subscriptionId = accessor.getSubscriptionId();
        String userId = accessor.getFirstNativeHeader("userId");

        log.info("userId= {}", userId);
        log.info("sessionId= {}", sessionId);
        log.info("subscriptionId: {}", subscriptionId);
        log.info("destination= {}", destination);




        sendEntryMessage(message);
    }


    private void sendEntryMessage(Message<?> message){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String destination = accessor.getDestination();
        String sessionId = accessor.getSessionId();
        String subscriptionId = accessor.getSubscriptionId();
        String userId = accessor.getFirstNativeHeader("userId");

        log.info("message= {}", message);

        log.info("destination= {}", destination);
        log.info("sessionId= {}", sessionId);
        log.info("subscriptionId: {}", subscriptionId);
        log.info("userId= {}", userId);



        Set<SimpUser> users = simpUserRegistry.getUsers();
        log.info("users: {}", users);

        Long roomId = getRoomId(destination);
        ChatMessageResponse msg = new ChatMessageResponse(Long.valueOf(userId), roomId, "입장!", LocalDateTime.now(), MessageType.ENTER);
        template.convertAndSend(destination, msg);
    }

    private Long getRoomId(String destination){
        int i = destination.lastIndexOf('/');
        Long roomId = Long.valueOf(destination.substring(i + 1));

        return roomId;
    }

    private void handlerDuplicate(String userId, String sessionId, String subscriptionId, String destination, SessionSubscribeEvent event ){
        /*int lastIndexOf = destination.lastIndexOf('/');
        String roomId = destination.substring(lastIndexOf + 1);
        log.info("roomId: {}", roomId);


        if(roomId.equals("error")){throw new RuntimeException("잘못된 구독입니다!");}*/

        SimpUser user = simpUserRegistry.getUser(userId);
        SimpSession session = user.getSession(sessionId);
        List<SimpSubscription> duplicatedSubscription = session.getSubscriptions()
                .stream()
                .filter((subscription) -> subscription.getDestination().equals(destination))
                .toList();
        // 이미 구독하고 있는 destination 으로 구독할 시 아무것도 x임.  하나만 있어야 함
        if(duplicatedSubscription.size() > 1){
            ((DefaultSimpUserRegistry)simpUserRegistry).onApplicationEvent(new SessionUnsubscribeEvent(this, event.getMessage(), user.getPrincipal()) );
//            throw new RuntimeException("중복 구독!");

            Map<String, Object> headers=new ConcurrentHashMap<>();
            headers.put(SimpMessageHeaderAccessor.MESSAGE_TYPE_HEADER, SimpMessageType.UNSUBSCRIBE);
            headers.put(SimpMessageHeaderAccessor.SESSION_ID_HEADER,sessionId);
            headers.put(SimpMessageHeaderAccessor.SUBSCRIPTION_ID_HEADER, subscriptionId);

            MessageHeaders messageHeaders = new MessageHeaders(headers);
            Byte[] payload=new Byte[0];
            GenericMessage<?> unsubscribeMessage = new GenericMessage<>(payload, messageHeaders);
            subscriptionRegistry.unregisterSubscription(unsubscribeMessage);

            log.info("중복 구독!");
            return;

        }
    }

}
