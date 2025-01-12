package com.example.chat_test.chat_user;

import com.example.chat_test.chat_user.entity.ChatUser;
import com.example.chat_test.exception.chat_room.ChatRoomErrorCode;
import com.example.chat_test.exception.chat_room.ChatRoomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class ChatUserSession {
    private static final Map<Long, Long> userAndRoom=new ConcurrentHashMap<>();

    public static boolean isActive(Long userId, Long roomId) {
        log.info("[ChatUserSession][isActive]");
        if(!userAndRoom.containsKey(userId)){
            return false;
        }
        else{
            Long findRoomId = userAndRoom.get(userId);
            if(!findRoomId.equals(roomId)){
                log.info("현재 활성화되어 있는 채팅방과 요청한 채팅방이 다릅니다.");
                return false;
            }
        }

        return true;
    }

    public static boolean isActive(Long userId) {
        log.info("[ChatUserSession][isActive]");
        return userAndRoom.containsKey(userId);
    }

    public static Long getRoomId(Long userId){
        log.info("[ChatUserSession][getRoomId]");
        return userAndRoom.get(userId);
    }



    // 웹소켓 연결이랑 구독 안 된 유저 채팅방 조회 시켜야 하나?
    public static void onRoom(Long userId, Long roomId){
        log.info("[ChatUserSession][onRoom]");
        if(userAndRoom.containsKey(userId)){
            log.info("이미 채팅방을 열었어.");
            throw new ChatRoomException(ChatRoomErrorCode.DUPLICATED_CHAT_ROOM);
        }
        else {
            userAndRoom.put(userId, roomId);
        }
    }
    
    public static void offRoom(Long userId, Long roomId){
        log.info("[ChatUserSession][offRoom]");
        if(userAndRoom.containsKey(userId)){
            Long findRoomId = userAndRoom.get(userId);
            log.info("현재 열여 있는 채팅방= {}", findRoomId);
            log.info("닫고 싶은 채팅방= {}", roomId);

            if(!roomId.equals(findRoomId)){
                log.warn("현재 열려 있는 채팅방과 닫고 싶은 채팅방이 다름. 일단 닫겠다.");
            }

            userAndRoom.remove(userId);
        }
        else{
            log.info("없는 채팅방을 왜 닫니?");
        }
    }

    /*public static void onRoom(ChatUser chatUser){
        onRoom(chatUser.getUser().getId(), chatUser.getChatRoom().getId());
        chatUser.updateLastAccessedAt();
    }*/


    public static void offRoom(ChatUser chatUser){
        offRoom(chatUser.getUser().getId(), chatUser.getChatRoom().getId());
    }
    
    // 채팅방 활성화한 유저의 수
    public static Long getUserCount(Long roomId){
        return userAndRoom.entrySet().stream()
                .filter(entry -> entry.getValue().equals(roomId)) // roomId와 일치하는 항목 필터링
                .count(); // 필터된 항목 개수 반환
    }
}
