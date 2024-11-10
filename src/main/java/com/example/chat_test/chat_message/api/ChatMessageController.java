package com.example.chat_test.chat_message.api;

import com.example.chat_test.chat_message.dto.response.ChatMessageResponse;
import com.example.chat_test.chat_message.entity.ChatMessage;
import com.example.chat_test.chat_message.service.ChatMessageService;
import com.example.chat_test.user.service.data.UserDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {
    private final ChatMessageService chatMessageService;



    @GetMapping("/api/rooms/{roomId}/messages")
    public ResponseEntity<Slice<ChatMessageResponse>> getMessages(@PathVariable("roomId") Long roomId,
                                                                  @RequestParam(value="page", required = false)Integer page,
                                                                  @RequestParam("userId")Long userId) {
        if(page == null) {
            page = 0;
        }

        UserDomain user = UserDomain.builder()
                .id(userId)
                .build();

        Slice<ChatMessageResponse> chatMessages = chatMessageService.getChatMessages(roomId, user, page, null);
        return ResponseEntity.ok(chatMessages);
    }
}
