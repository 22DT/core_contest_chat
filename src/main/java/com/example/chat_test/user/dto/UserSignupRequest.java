package com.example.chat_test.user.dto;

public record UserSignupRequest(
        String nickname,
        Long teamId
) {
}
