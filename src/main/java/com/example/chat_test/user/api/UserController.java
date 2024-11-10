package com.example.chat_test.user.api;

import com.example.chat_test.user.dto.UserSignupRequest;
import com.example.chat_test.user.service.UserService;
import com.example.chat_test.user.service.data.UserDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @GetMapping("/api/users/")
    public ResponseEntity<List<UserDomain>> getUsers(@RequestParam("roomId")Long roomId){
        List<UserDomain> usersByRoom = userService.getUsersByRoom(roomId);
        return ResponseEntity.ok(usersByRoom);
    }

    @PostMapping("/api/users/signup")
    public ResponseEntity<Long> signup(@RequestBody UserSignupRequest userSignupRequest) {
        Long signup = userService.signup(userSignupRequest.nickname(), userSignupRequest.teamId());

        return ResponseEntity.ok(signup);
    }


}
