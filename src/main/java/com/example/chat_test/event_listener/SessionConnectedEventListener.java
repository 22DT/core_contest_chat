package com.example.chat_test.event_listener;

import com.sun.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.DefaultSimpUserRegistry;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class SessionConnectedEventListener {
    private final SimpUserRegistry simpUserRegistry;
    private Long seq=0L;

    /**
     *
     * @param event
     *
     * @apiNote
     * 토큰 대신 header 에 userId 사용하자.
     *
     */

    @EventListener
    public void onSessionConnected(SessionConnectedEvent event) {
        log.info("[SessionConnectedEventListener][onSessionConnected]");
        StompHeaderAccessor accessor = getNativeHeaderAccessor(event.getMessage());
        String userId = accessor.getFirstNativeHeader("userId");
        log.info("userId= {}", userId);

        UserPrincipal principal = new UserPrincipal(userId);
        ((DefaultSimpUserRegistry)simpUserRegistry).onApplicationEvent(new SessionConnectedEvent(this, event.getMessage(), principal));

        Set<SimpUser> users = simpUserRegistry.getUsers();
        log.info("users= {}", users);
    }


    private StompHeaderAccessor getNativeHeaderAccessor(Message<byte[]> message) {
        MessageHeaders headers = message.getHeaders();
        Message<?> simpConnectMessage = (Message<?>) headers.get("simpConnectMessage");
        return StompHeaderAccessor.wrap(simpConnectMessage);
    }



}
