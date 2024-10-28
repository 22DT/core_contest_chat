package com.example.chat_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatTestApplication.class, args);
	}

}


/*
*

insert into users(team_id, user_id, nickname) values(1, 1, '짱구');
insert into users(team_id, user_id, nickname) values(1, 2, '철수');
insert into users(team_id, user_id, nickname) values(1, 3, '맹구');
insert into users(team_id, user_id, nickname) values(2, 4, '훈이');
insert into users(team_id, user_id, nickname) values(2, 5, '흰둥이');

*
* */