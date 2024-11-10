package com.example.chat_test.user.entity;

import com.example.chat_test.user.service.data.UserDomain;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access= PROTECTED)
@Builder
@AllArgsConstructor(access=PROTECTED)
@Getter
@Table(name="users")
public class User {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name="user_Id")
    private Long id;

    private Long teamId;

    private String nickname;
    private String snsProfileImageUrl;



    public UserDomain toDomain(){
        return UserDomain.builder()
                .id(id)
                .nickname(nickname)
                .teamId(teamId)
                .build();
    }
}
