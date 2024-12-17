package com.example.chat_test.chat_message.api;

import com.example.chat_test.chat_message.dto.response.ChatMessageResponse;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {
    private final ChatMessageService chatMessageService;



    @GetMapping("/api/rooms/{room-id}/messages")
    public ResponseEntity<Slice<ChatMessageResponse>> getMessages(@PathVariable("room-id") Long roomId,
                                                                  @RequestParam(value="page", required = false)Integer page,
                                                                  @RequestParam("userId")Long userId) {
        if(page == null) {
            page = 0;
        }

        UserDomain user = UserDomain.builder()
                .id(userId)
                .build();

        // 이거 왜 lastAccessedAt null 이지? 그리고 이거 받을 필요가 있어?
        Slice<ChatMessageResponse> chatMessages = chatMessageService.getChatMessages(roomId, user, page, null);
        return ResponseEntity.ok(chatMessages);
    }

    @GetMapping("/api/rooms/{room-id}/images")
    public ResponseEntity<List<ChatMessageResponse>> getImage(@PathVariable("room-id")Long roomId,
                                                              @RequestParam("userId")Long userId){
        UserDomain user = UserDomain.builder()
                .id(userId)
                .build();

        List<ChatMessageResponse> images = chatMessageService.getImages(roomId, user);
        return ResponseEntity.ok(images);
    }
}
