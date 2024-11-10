package com.example.chat_test.chat_room.repository;

import com.example.chat_test.chat_room.ChatRoomType;
import com.example.chat_test.chat_room.entity.ChatRoom;
import com.example.chat_test.chat_room.entity.NewMessageRoom;
import com.example.chat_test.chat_room.service.ChatRoomRepository;
import com.example.chat_test.chat_user.entity.ChatUser;
import com.example.chat_test.chat_user.repository.ChatUserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChatRoomRepositoryImpl implements ChatRoomRepository {
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final NewChatMessageRoomJpaRepository newChatMessageRoomJpaRepository;
    private final ChatUserJpaRepository chatUserJpaRepository;


    @Override
    public Long createChatRoom(ChatRoomType type) {
        ChatRoom chatRoom = ChatRoom.builder()
                .type(type)
                .build();

        return chatRoomJpaRepository.save(chatRoom).getId();
    }

    @Override
    public void createNewMessageRoom(List<Long> chatUserIds, Long roomId) {
        log.info("[ChatRoomRepositoryImpl][createNewMessageRoom]");
        log.info("roomId= {}", roomId);

        ChatRoom chatRoom = chatRoomJpaRepository.getReferenceById(roomId);
        List<NewMessageRoom> rooms = new ArrayList<>();

        for (Long chatUserId : chatUserIds) {
            ChatUser chatUser = chatUserJpaRepository.getReferenceById(chatUserId);
            NewMessageRoom newMessageRoom = NewMessageRoom.builder()
                    .chatRoom(chatRoom)
                    .chatUser(chatUser)
                    .build();

            rooms.add(newMessageRoom);
        }
        for (NewMessageRoom room : rooms) {
            log.info("ChatUserId= {}", room.getChatUser().getId());
            log.info("roomId= {}", room.getChatRoom().getId());
        }

        newChatMessageRoomJpaRepository.saveAll(rooms);
    }

    @Override
    public ChatRoom getChatRoom(Long chatRoomId) {
        ChatRoom room = chatRoomJpaRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat Room Not Found"));

        return room;
    }

    @Override
    public List<ChatRoom> getChatRooms(Long userId) {
        return chatRoomJpaRepository.findChatRoomsByUserId(userId);
    }

    @Override
    public void deleteNewMessageRoom(Long chatRoomId, Long chatUserId) {
        newChatMessageRoomJpaRepository.deleteByChatRoomIdAndChatUserId(chatRoomId, chatUserId);
    }

    @Override
    public boolean existsNewMessageChatRoom(Long chatRoomId, Long chatUserId) {
        return newChatMessageRoomJpaRepository.existsNewMessageRoomByChatRoomIdAndChatUserId(chatRoomId, chatUserId);
    }

}
