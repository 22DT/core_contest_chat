package com.example.chat_test.chat_user.service.db;

import com.example.chat_test.chat_room.entity.ChatRoom;
import com.example.chat_test.chat_room.service.ChatRoomRepository;
import com.example.chat_test.chat_user.entity.ChatUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatUserReader {
    private final ChatRoomRepository chatRoomRepository;

    public ChatUser getPrivateChatRoomPartner(Long userId, Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.getPrivateChatRoom(roomId);
        List<ChatUser> chatUsers = chatRoom.getChatUsers();

        for (ChatUser chatUser : chatUsers) {
            Long findId = chatUser.getUser().getId();

            if (!findId.equals(userId)) {
                return chatUser;
            }
        }

        return null;
    }
}
