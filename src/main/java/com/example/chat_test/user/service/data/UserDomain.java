package com.example.chat_test.user.service.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Builder
@AllArgsConstructor(access = PROTECTED)
public class UserDomain {
    private Long id;
    private String nickname;
    private Long teamId;
    private String snsProfileImageUrl;

}
