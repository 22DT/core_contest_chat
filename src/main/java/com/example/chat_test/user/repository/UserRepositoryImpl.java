package com.example.chat_test.user.repository;

import com.example.chat_test.user.entity.User;
import com.example.chat_test.user.service.UserRepository;
import com.example.chat_test.user.service.data.UserDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;
    @Override
    public Long save(String nickname, Long teamId) {
        User user = User.builder()
                .nickname(nickname)
                .teamId(teamId)
                .build();
        return userJpaRepository.save(user).getId();

    }

    @Override
    public UserDomain getUser(Long userId) {
        User one = userJpaRepository.findById(userId).orElse(null);
        return one.toDomain();
    }

    @Override
    public List<UserDomain> getUsersByRoomId(Long roomId) {

        return userJpaRepository.findUsersByRoomId(roomId)
                .stream()
                .map(User::toDomain)
                .toList();
    }

    @Override
    public List<UserDomain> getUsers(Long teamId) {
        return userJpaRepository.findUsersByTeamId(teamId).stream()
                .map(User::toDomain).toList();
    }


}
