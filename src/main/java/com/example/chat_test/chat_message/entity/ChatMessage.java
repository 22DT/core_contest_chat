package com.example.chat_test.chat_message.entity;

import com.example.chat_test.chat_message.MessageType;
import com.example.chat_test.chat_room.entity.ChatRoom;
import com.example.chat_test.chat_user.entity.ChatUser;
import com.example.chat_test.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Builder
@AllArgsConstructor(access = PROTECTED)
@Getter
public class ChatMessage {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "chat_message")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="writer_id")
    private ChatUser Writer;

    private String message;
    private LocalDateTime createdAt;
    private MessageType messageType;

}
