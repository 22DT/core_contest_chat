package com.example.chat_test.chat_user.entity;

import com.example.chat_test.chat_room.entity.ChatRoom;
import com.example.chat_test.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.InheritanceType.JOINED;
import static lombok.AccessLevel.PROTECTED;

@Builder
@AllArgsConstructor(access=PROTECTED)
@Getter
@Entity
@NoArgsConstructor(access= PROTECTED)
@Inheritance(strategy=JOINED)
public class ChatUser {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name="chat_user_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="chat_room_id")
    private ChatRoom chatRoom;

    private LocalDateTime lastAccessedAt;
    private LocalDateTime lastJoinedAt;

//    private Long lastReadMessageId;
//    private Long firstReadMessageId;

    private boolean isLeave;
    private boolean isNoti;

//    private String imageUrl;   // 개인일 경우 상대방 프사, 팀일 경우 어떤 사진?

    public void flipNoti(){
        isNoti= !isNoti;
    }

    public void leaveRoom(){
        isLeave = true;
    }

    public void updateLastAccessedAt(){
        this.lastAccessedAt = LocalDateTime.now();
    }

    public void joinChatRoom(){
        isLeave = false;
        lastJoinedAt = LocalDateTime.now();
    }


}
