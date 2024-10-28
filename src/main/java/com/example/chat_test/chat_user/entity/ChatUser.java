package com.example.chat_test.chat_user.entity;

import com.example.chat_test.chat_room.entity.ChatRoom;
import com.example.chat_test.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access= PROTECTED)
@Builder
@AllArgsConstructor(access=PROTECTED)
@Getter
public class ChatUser {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="chat_room_id")
    private ChatRoom chatRoom;

}
