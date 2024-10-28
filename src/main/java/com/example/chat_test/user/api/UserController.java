package com.example.chat_test.user.api;

import com.example.chat_test.user.dto.UserSignupRequest;
import com.example.chat_test.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/api/users/signup")
    public ResponseEntity<Long> signup(@RequestBody UserSignupRequest userSignupRequest) {
        Long signup = userService.signup(userSignupRequest.nickname(), userSignupRequest.teamId());

        return ResponseEntity.ok(signup);
    }


}
