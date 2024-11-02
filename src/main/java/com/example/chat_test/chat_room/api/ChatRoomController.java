package com.example.chat_test.chat_room.api;

import com.example.chat_test.chat_room.ChatRoomType;
import com.example.chat_test.chat_room.entity.ChatRoom;
import com.example.chat_test.chat_room.service.ChatRoomService;
import com.example.chat_test.user.service.data.UserDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;


    @PostMapping("/api/group")
    public ResponseEntity<Long> createCroupChatRoom(@RequestParam("userId")Long userId,
                                                    @RequestParam("teamId")Long teamId) {
        // 임시로
        UserDomain user = UserDomain.builder()
                .id(userId)
                .build();

        Long chatRoomId = chatRoomService.createGroupChatRoom(user, teamId);
        return ResponseEntity.ok(chatRoomId);
    }

    @PostMapping("/api/private")
    public ResponseEntity<Long> createPrivateChatRoom(@RequestParam("userId")Long userId,
                                                      @RequestParam("targetUserId")Long targetUserId) {
        UserDomain user = UserDomain.builder()
                .id(userId)
                .build();

        Long privateChatRoom = chatRoomService.createPrivateChatRoom(user, targetUserId);
        return ResponseEntity.ok(privateChatRoom);
    }


    @GetMapping("/api/chat-rooms-list")
    public ResponseEntity<List<ChatRoom>> getChatRooms() {
        List<ChatRoom> chatRooms = chatRoomService.getChatRooms();
        return ResponseEntity.ok(chatRooms);
    }
}
