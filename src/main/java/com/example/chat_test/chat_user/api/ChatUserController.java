package com.example.chat_test.chat_user.api;

import com.example.chat_test.chat_user.dto.response.ChatUserResponse;
import com.example.chat_test.chat_user.entity.ChatUser;
import com.example.chat_test.chat_user.service.ChatUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatUserController {
    private final ChatUserService chatUserService;


    @GetMapping("/test/chat-users")
    public ResponseEntity<List<ChatUserResponse>> getChatUsers(@RequestParam("roomId") Long roomId) {
        List<ChatUserResponse> chatUsers = chatUserService.getChatUsers(roomId);
        return ResponseEntity.ok(chatUsers);
    }
}
