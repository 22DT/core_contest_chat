package com.example.chat_test.chat_room.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Builder
@AllArgsConstructor(access = PROTECTED)
public class Contest {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="contest_id")
    private Long id;

    private String title;

}
