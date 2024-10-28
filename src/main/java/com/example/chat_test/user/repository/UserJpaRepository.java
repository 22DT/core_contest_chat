package com.example.chat_test.user.repository;

import com.example.chat_test.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    List<User> findUsersByTeamId(Long teamId);

}
