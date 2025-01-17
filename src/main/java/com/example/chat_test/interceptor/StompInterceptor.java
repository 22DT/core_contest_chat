package com.example.chat_test.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Component
public class StompInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        String userId = accessor.getFirstNativeHeader("userId");

        log.info("[StompInterceptor][preSend]");
        log.info("command= {}" ,command);
        log.info("userId= {}", userId);
        log.info("channel= {}", channel);

        if (StompCommand.CONNECT == accessor.getCommand()) { // websocket 연결요청


        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) { // 채팅룸 구독요청

        } else if (StompCommand.DISCONNECT == accessor.getCommand()) { // Websocket 연결 종료

        }
        return message;
    }
}
