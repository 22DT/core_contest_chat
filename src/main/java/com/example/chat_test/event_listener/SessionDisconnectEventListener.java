package com.example.chat_test.event_listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class SessionDisconnectEventListener {


    @EventListener
    public void onSessionDisconnect(SessionUnsubscribeEvent event) {
        log.info("[SessionDisconnectEventListener][onSessionDisconnect]");

        Message<byte[]> message = event.getMessage();
        log.info("message= {}", message);

    }
}
