package com.example.chat_test.user.service;

import com.example.chat_test.user.entity.User;
import com.example.chat_test.user.service.data.UserDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public Long signup(String nickname, Long teamId){
        return userRepository.save(nickname, teamId);
    }

    public List<UserDomain> getUsersByTeam(Long teamId) {
        return userRepository.getUsers(teamId);
    }

}
