package com.example.chat_test.chat_room.entity;

import com.example.chat_test.chat_room.ChatRoomType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access= PROTECTED)
@Builder
@AllArgsConstructor(access=PROTECTED)
@Getter
public class ChatRoom {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(STRING)
    private ChatRoomType type;

}
