package com.example.chat_test.chat_room.entity;

import com.example.chat_test.chat_room.ChatRoomType;
import com.example.chat_test.chat_user.entity.ChatUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access= PROTECTED)
@Builder
@AllArgsConstructor(access=PROTECTED)
@Getter
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @Enumerated(STRING)
    private ChatRoomType type;

    @OneToOne(fetch=LAZY)
    @JoinColumn(name="contest_id")
    private Contest contest;


    @Builder.Default
    @OneToMany(mappedBy = "chatRoom", fetch = LAZY)
    private List<ChatUser> chatUsers = new ArrayList<>();

}
