package com.example.chat_test.chat_message.api;

import com.example.chat_test.chat_message.dto.request.ChatMessageRequest;
import com.example.chat_test.chat_message.dto.response.ChatMessageResponse;
import com.example.chat_test.chat_message.service.ChatMessageService;
import com.example.chat_test.user.service.data.UserDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController  {
    private final SimpMessageSendingOperations template;
    private final ChatMessageService chatMessageService;



    @MessageMapping("/topic")
//    @SendTo("topic/greetings")
    public void topic(@Payload ChatMessageRequest chatMessage) {
        log.info("[ChatController][topic]");
        log.info("chatMessage: {}", chatMessage);

        // 임시로
        UserDomain user = UserDomain.builder()
                .id(chatMessage.userId())
                .build();

        ChatMessageResponse chatMessageResponse = chatMessageService.save(chatMessage, user);

        template.convertAndSend("/topic/" + chatMessageResponse.roomId(), chatMessageResponse);
    }

    @MessageMapping("/queue")
    public void queue(@Payload ChatMessageRequest chatMessage) {
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





}
