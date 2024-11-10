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
insert into users(team_id, user_id, nickname) values(1, 2, '짱아');
insert into users(team_id, user_id, nickname) values(1, 3, '흰둥이');
insert into users(team_id, user_id, nickname) values(2, 4, '철수');
insert into users(team_id, user_id, nickname) values(2, 5, '맹구');
insert into users(team_id, user_id, nickname) values(3, 6, '훈이');

insert into chat_room(chat_room_id, type) values(1, 'GROUP');
insert into chat_room(chat_room_id, type) values(2, 'GROUP');
insert into chat_room(chat_room_id, type) values(3, 'GROUP');
insert into chat_room(chat_room_id, type) values(4, 'GROUP');
insert into chat_room(chat_room_id, type) values(5, 'GROUP');

insert into chat_user(chat_room_id, chat_user_id, user_id) values(1, 1, 1);
insert into chat_user(chat_room_id, chat_user_id, user_id) values(2, 2, 1);
insert into chat_user(chat_room_id, chat_user_id, user_id) values(3, 3, 1);
insert into chat_user(chat_room_id, chat_user_id, user_id) values(4, 4, 1);
insert into chat_user(chat_room_id, chat_user_id, user_id) values(5, 5, 1);

INSERT INTO chat_message(chat_message_id, created_at, room_id, writer_id, message, message_type) VALUES (1, NOW(), 1, 1, 'ㄱㄱ', 'TALK');
INSERT INTO chat_message(chat_message_id, created_at, room_id, writer_id, message, message_type) VALUES (2, NOW(), 1, 1, 'ㄴㄴ', 'TALK');
INSERT INTO chat_message(chat_message_id, created_at, room_id, writer_id, message, message_type) VALUES (3, NOW(), 1, 1, 'ㄷㄷ', 'TALK');

INSERT INTO chat_message(chat_message_id, created_at, room_id, writer_id, message, message_type) VALUES (4, NOW(), 2, 1, 'ㄱㄱ', 'TALK');
INSERT INTO chat_message(chat_message_id, created_at, room_id, writer_id, message, message_type) VALUES (5, NOW(), 2, 1, 'ㄴㄴ', 'TALK');
INSERT INTO chat_message(chat_message_id, created_at, room_id, writer_id, message, message_type) VALUES (6, NOW(), 2, 1, 'ㄷㄷ', 'TALK');

INSERT INTO chat_message(chat_message_id, created_at, room_id, writer_id, message, message_type) VALUES (7, NOW(), 3, 1, 'ㄱㄱ', 'TALK');
INSERT INTO chat_message(chat_message_id, created_at, room_id, writer_id, message, message_type) VALUES (8, NOW(), 3, 1, 'ㄴㄴ', 'TALK');
INSERT INTO chat_message(chat_message_id, created_at, room_id, writer_id, message, message_type) VALUES (9, NOW(), 3, 1, 'ㄷㄷ', 'TALK');

INSERT INTO chat_message(chat_message_id, created_at, room_id, writer_id, message, message_type) VALUES (10, NOW(), 4, 1, 'ㄱㄱ', 'TALK');
INSERT INTO chat_message(chat_message_id, created_at, room_id, writer_id, message, message_type) VALUES (11, NOW(), 4, 1, 'ㄴㄴ', 'TALK');
INSERT INTO chat_message(chat_message_id, created_at, room_id, writer_id, message, message_type) VALUES (12, NOW(), 4, 1, 'ㄷㄷ', 'TALK');

INSERT INTO chat_message(chat_message_id, created_at, room_id, writer_id, message, message_type) VALUES (13, NOW(), 5, 1, 'ㄱㄱ', 'TALK');
INSERT INTO chat_message(chat_message_id, created_at, room_id, writer_id, message, message_type) VALUES (14, NOW(), 5, 1, 'ㄴㄴ', 'TALK');
INSERT INTO chat_message(chat_message_id, created_at, room_id, writer_id, message, message_type) VALUES (15, NOW(), 5, 1, 'ㄷㄷ', 'TALK');


SELECT DISTINCT room_id
FROM CHAT_MESSAGE
ORDER BY room_id, created_at DESC;



*
* */