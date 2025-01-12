package com.example.chat_test.chat_message.entity;

import com.example.chat_test.chat_message.MessageType;
import com.example.chat_test.chat_room.entity.ChatRoom;
import com.example.chat_test.chat_user.entity.ChatUser;
import com.example.chat_test.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Builder
@AllArgsConstructor(access = PROTECTED)
@Getter
public class ChatMessage {

    // **무조건 자동 증가 전략 사용해야 함.**
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "chat_message_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="chat_user_id")
    private ChatUser chatUser;

    private String message;

    @Enumerated(STRING)
    private MessageType messageType;

    private Integer readCount;
    private LocalDateTime createdAt;



}
