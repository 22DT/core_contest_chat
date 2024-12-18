package com.example.chat_test.chat_message.service.db;

import com.example.chat_test.chat_message.entity.ChatMessage;
import com.example.chat_test.chat_message.service.ChatMessageRepository;
import com.example.chat_test.chat_user.entity.ChatUser;
import com.example.chat_test.chat_user.service.ChatUserRepository;
import com.example.chat_test.user.service.UserRepository;
import com.example.chat_test.user.service.data.UserDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatMessageReader {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatUserRepository chatUserRepository;

    // 참가자인지 검증해야 하나??


    public Slice<ChatMessage> getChatMessages(Long roomId, UserDomain user, Integer page, LocalDateTime lastAccessedAt){
        log.info("[ChatMessageReader][getChatMessages]");
        ChatUser chatUser = chatUserRepository.getChatUser(roomId, user.getId());

        // 메시지들 조회
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        if(lastAccessedAt ==null){

            lastAccessedAt=chatUser.getLastAccessedAt();
        }

        log.info("lastAccessedAt= {}", lastAccessedAt);
        return chatMessageRepository.getChatMessages(roomId, chatUser.getLastJoinedAt(), lastAccessedAt, pageable);
    }

    public List<ChatMessage> getImages(Long roomId, UserDomain user){
        ChatUser chatUser = chatUserRepository.getChatUser(roomId, user.getId());
        List<ChatMessage> images = chatMessageRepository.getImages(roomId, chatUser.getLastJoinedAt());

        return images;
    }




}
