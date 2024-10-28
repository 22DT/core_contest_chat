package com.example.chat_test.user.service;

import com.example.chat_test.user.entity.User;
import com.example.chat_test.user.service.data.UserDomain;

import java.util.List;

public interface UserRepository {
     Long save(String nickname, Long teamId);
     UserDomain getUser(Long userId);

     List<UserDomain> getUsers(Long teamId);

}
