package com.example.chat_test.chat_room.entity;

import com.example.chat_test.chat_user.entity.ChatUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Builder
@AllArgsConstructor(access = PROTECTED)
@Getter
public class NewMessageRoom {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "new_message_room_id")
    private Long id;

    @ManyToOne(fetch=LAZY)
    @JoinColumn(name="chat_user_id")
    private ChatUser chatUser;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="chat_room_id")
    private ChatRoom chatRoom;
}
