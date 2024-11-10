package com.example.chat_test.user.repository;

import com.example.chat_test.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    List<User> findUsersByTeamId(Long teamId);

    @Query("SELECT cu.user " +
            "FROM ChatUser cu " +
         //   "JOIN FETCH cu.user " +
            "WHERE cu.chatRoom.id = :roomId")
    List<User> findUsersByRoomId(@Param("roomId") Long roomId);


}
