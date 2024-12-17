package com.example.chat_test.event_listener;

import com.example.chat_test.chat_user.entity.ChatUser;
import com.example.chat_test.chat_user.service.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class SessionUnsubscribeEventListener {
    private final ChatUserRepository chatUserRepository;


    /**
     *
     * @param event
     *
     * @apiNote
     */
//    @EventListener
    public void onSessionUnsubscribe(SessionUnsubscribeEvent event) {
        log.info("[SessionUnsubscribeEventListener][onSessionUnsubscribe]");
        Message<byte[]> message = event.getMessage();
        MessageHeaders headers = message.getHeaders();

        String stringUserId = SimpMessageHeaderAccessor.getFirstNativeHeader("userId", headers);
        String stringRoomId = SimpMessageHeaderAccessor.getFirstNativeHeader("roomId", headers);
        log.info("stringUserId= {}", stringUserId);
        log.info("stringRoomId= {}", stringRoomId);


        if(!Objects.requireNonNull(stringRoomId).isEmpty() && !Objects.requireNonNull(stringUserId).isEmpty()){
            Long userId = Long.valueOf(Objects.requireNonNull(stringUserId));
            Long roomId = Long.valueOf(Objects.requireNonNull(stringRoomId));

            log.info("userId= {}", userId);
            log.info("roomId= {}", roomId);

        }
        else{

        }


    }

    /*
    log.info("stringRoomId is null");
            log.info("Disconnection 으로 인한 unsub?");

    List<ChatUser> chatUsers = chatUserRepository.getChatUsersByUserId(userId);

    // 쿼리 하나로 처리할 수 있음.
            for (ChatUser chatUser : chatUsers) {
        chatUser.activeOff();
    }
    */


}
