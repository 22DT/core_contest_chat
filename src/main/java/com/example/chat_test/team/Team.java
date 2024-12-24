package com.example.chat_test.team;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Builder
@AllArgsConstructor(access = PROTECTED)
public class Team {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="team_id")
    private Long id;

    private String name;

}
