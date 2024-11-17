package com.example.chat_test.chat_room.api;

import com.example.chat_test.chat_room.dto.response.ChatRoomPreviewResponse;
import com.example.chat_test.chat_room.dto.response.ChatRoomResponse;
import com.example.chat_test.chat_room.service.ChatRoomService;
import com.example.chat_test.chat_user.ChatUserSession;
import com.example.chat_test.user.service.data.UserDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final SimpUserRegistry simpUserRegistry;

    @PostMapping("/api/group")
    public ResponseEntity<Long> createGroupChatRoom(@RequestParam("userId")Long userId,
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

    /**
     *
     * @param roomId
     * @param userId
     * @return
     *
     * @apiNote
     * 이미 채팅방 켜놓은 애 이거 막아야 함.
     */
    @GetMapping("/api/chat-rooms/{room-id}")
    public ResponseEntity<ChatRoomResponse> getChatRoom(@PathVariable("room-id") Long roomId,
                                            @RequestParam("userId")Long userId) {

        UserDomain user = UserDomain.builder()
                .id(userId)
                .build();


        ChatRoomResponse chatRoom = chatRoomService.getChatRoom(roomId, user);
        return ResponseEntity.ok(chatRoom);
    }

    @PostMapping("/api/rooms/{room-id}/close")
    public ResponseEntity<Void> closeRoom(@PathVariable("room-id") Long roomId,
                                          @RequestParam("userId")Long userId){

        UserDomain user = UserDomain.builder()
                .id(userId)
                .build();

        ChatUserSession.offRoom(userId, roomId);


        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/chat-rooms-ids")
    public ResponseEntity<List<Long>> getChatRoomIds(@RequestParam("userId")Long userId) {

        UserDomain user = UserDomain.builder()
                .id(userId)
                .build();

        List<Long> roomIds=chatRoomService.getChatRoomIds(user);

        return ResponseEntity.ok().body(roomIds);
    }


    @GetMapping("/api/chat-rooms-list")
    public ResponseEntity<List<ChatRoomPreviewResponse>> getChatRooms(@RequestParam("userId")Long userId) {
        UserDomain user = UserDomain.builder()
                .id(userId)
                .build();

        List<ChatRoomPreviewResponse> chatRooms = chatRoomService.getChatRooms(user);

        return ResponseEntity.ok(chatRooms);
    }


    /**
     *
     * @param userId
     *
     * 1: 채팅방 조회 세션연결 세션 종료 채팅방 닫기
     * 2: 세션 연결 채팅방 조회 채팅방 닫디 세션 종료
     *
     * 어떤 게 좋을까?
     */

}
