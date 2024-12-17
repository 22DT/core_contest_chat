package com.example.chat_test.chat_room.service;

import com.example.chat_test.chat_message.MessageType;
import com.example.chat_test.chat_message.dto.response.ChatMessageResponse;
import com.example.chat_test.chat_message.entity.ChatMessage;
import com.example.chat_test.chat_message.service.ChatMessageRepository;
import com.example.chat_test.chat_message.service.ChatMessageUtil;
import com.example.chat_test.chat_message.service.db.ChatMessageReader;
import com.example.chat_test.chat_room.ChatRoomType;
import com.example.chat_test.chat_room.dto.response.ChatRoomPreviewResponse;
import com.example.chat_test.chat_room.dto.response.ChatRoomResponse;
import com.example.chat_test.chat_room.entity.ChatRoom;
import com.example.chat_test.chat_user.ChatUserSession;
import com.example.chat_test.chat_user.entity.ChatUser;
import com.example.chat_test.chat_user.service.ChatUserRepository;
import com.example.chat_test.exception.chat_room.ChatRoomErrorCode;
import com.example.chat_test.exception.chat_room.ChatRoomException;
import com.example.chat_test.exception.chat_user.ChatUserErrorCode;
import com.example.chat_test.exception.chat_user.ChatUserException;
import com.example.chat_test.user.service.UserService;
import com.example.chat_test.user.service.data.UserDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;
    private final ChatUserRepository chatUserRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageReader chatMessageReader;


    // 중복 생성 막아야 해.
    public Long createGroupChatRoom(UserDomain user ,Long teamId) {
        // team 의 유저를 모두 찾아온다. (팀장 포함?)
        List<UserDomain> teamUsers = userService.getUsersByTeam(teamId);

        // 채팅방 생성
        Long chatRoomId = chatRoomRepository.createChatRoom(ChatRoomType.GROUP);
        // 채팅 유저 생성.
        chatUserRepository.saveCharUsers(teamUsers,chatRoomId);
        chatMessageRepository.sendRoomCreationMessage(chatRoomId);

        return chatRoomId ;
    }

    /**
     *
     * @param user
     * @param targetUserId
     * @return
     *
     * @apiNote
     *
     * 똑같은 사람이랑 중복 채팅방 생성 허용?
     * 상황: A, B 개인 채팅 중. A 나감 A, B 다시 채팅창 만듦. B는 A와의 기존 채팅방과 새 채팅방 두 개 전부 유지?
     *
     * 1) 둘 다 처음
     * 2) 나는 나감. 상대방 안 나감.
     * 3) 둘 다 나감.
     *
     */
    public Long createPrivateChatRoom(UserDomain user ,Long targetUserId) {
        UserDomain targetUser = userService.getUserById(targetUserId);
        List<ChatUser> myChatUsers=chatUserRepository.getPrivateChatUser(user.getId());
        List<ChatUser> targetChatUsers=chatUserRepository.getPrivateChatUser(targetUserId);

        // 일단 둘의 개인 채빙방이 있는지 확인해야 함.

        if(Objects.equals(user.getId(), targetUserId)){
            throw new ChatRoomException(ChatRoomErrorCode.CANNOT_CREATE_SINGLE_CHAT_ROOM);
        }

        Long roomId = null;
        ChatUser myChatUser = null;
        ChatUser targetChatUser = null;
        for (ChatUser mCU : myChatUsers) {
            Long chatRoomId=mCU.getChatRoom().getId();
            boolean isFind=false;
            for (ChatUser tCU : targetChatUsers) {
                if(chatRoomId.equals(tCU.getChatRoom().getId())) {
                    roomId=tCU.getChatRoom().getId();
                    isFind=true;
                    myChatUser=mCU;
                    targetChatUser=tCU;
                    break;
                }
            }

            if(isFind) {break;}
        }

        if(roomId==null){
            // 같이 있는 채팅방이 없어. 1) or 3)

            // 채팅방 생성
            Long chatRoomId = chatRoomRepository.createChatRoom(ChatRoomType.PRIVATE);
            // 채팅 유저 생성.
            chatUserRepository.saveCharUsers(List.of(user,targetUser),chatRoomId);

            chatMessageRepository.sendRoomCreationMessage(chatRoomId);

            return chatRoomId;
        }
        else{
            // 나 나감 and 상대 안 나감.
            if(myChatUser.isLeave() && !targetChatUser.isLeave()){
                myChatUser.joinChatRoom();
            }
            // 나 안 나감
            else if(!myChatUser.isLeave()){
                throw new ChatRoomException(ChatRoomErrorCode.DUPLICATED_CHAT_ROOM);
            }

            return myChatUser.getChatRoom().getId();
        }
    }


    /**
     *
     * @param chatRoomId
     * @param user
     * @return
     *
     * @apiNote
     * 구독 유무 판단해야 하나? 이거 안 하면 readCount 흠흠..
     *
     * 1) ChatUserSession 에 추가.
     * 2) lastAccessedAt 갱신
     * 3) 안 읽은 메시지 읽음 처리.
     *
     * 나간 거 처리해주기....
     *
     */
    public ChatRoomResponse getChatRoom(Long chatRoomId, UserDomain user) {
        log.info("[ChatRoomService][getChatRoom]");
        Long userId = user.getId();
        // chatUser 찾아온다.
        List<ChatUser> chatUsers = chatUserRepository.getChatUsers(chatRoomId);
        ChatUser chatUser = null;

        for (ChatUser cu : chatUsers) {
            if(Objects.equals(cu.getUser().getId(), user.getId())){
                chatUser = cu;
                break;
            }
        }
        if(chatUser==null){
            throw new ChatUserException(ChatUserErrorCode.USER_NOT_PARTICIPANT);
        }

        if(chatUser.isLeave()){
            throw new ChatRoomException(ChatRoomErrorCode.CANNOT_VIEW_LEFT_CHAT_ROOM);
        }

        // 마지막 접속 시간 갱신.
        boolean active = ChatUserSession.isActive(userId, chatRoomId);
        if(active){throw new ChatRoomException(ChatRoomErrorCode.DUPLICATED_CHAT_ROOM);}
        ChatUserSession.onRoom(userId, chatRoomId);

        LocalDateTime oldTime=chatUser.getLastAccessedAt();
        chatUser.updateLastAccessedAt();
        LocalDateTime newTime = chatUser.getLastAccessedAt();

        log.info("oldAccessedTime= {}", oldTime);
        log.info("newAccessedTime= {}", newTime);


        // 읽지 않은 메시지 읽음 처리.
        Integer maxReadCount=chatUsers.size();
        chatMessageRepository.incrementUnreadMessageCount(chatRoomId, chatUser.getId(), newTime, oldTime,  maxReadCount);

        // 메시지 갖고 온다.
        Slice<ChatMessage> chatMessages = chatMessageReader.getChatMessages(chatRoomId, user, 0, chatUser.getLastAccessedAt());

        Slice<ChatMessageResponse> chatMessageResponses = ChatMessageUtil.chatMessageToResponse(chatMessages);

        return new ChatRoomResponse(
                chatRoomId,
                "roomName",
                chatMessageResponses
        );

    }


    /*
    * 이거 연결할 때 쫙 뿌리고 싶은데
    * 그러기 위해서는 구독이 아니라 세션으로 메시지 전송해야 함.
    * -> nofi 용 topic 구독시키자.
    * stomp 사용 x 임.
    *
    * */
    public List<Long> getChatRoomIds(UserDomain user){
        List<ChatUser> chatUsers = chatUserRepository.getChatUsersByUserId(user.getId());

        // 나가지 않은 방만 return 해줘야 함.
        List<Long> chatRoomIds = new ArrayList<>();
        for (ChatUser chatUser : chatUsers) {
            if(!chatUser.isLeave()){
                chatRoomIds.add(chatUser.getId());
            }
        }

        return chatRoomIds;

    }


    public List<ChatRoomPreviewResponse> getChatRooms(UserDomain user) {
        log.info("[ChatRoomService][getChatRooms]");

        // 채팅유저를 갖고 온다.
        List<ChatUser> chatUsers = chatUserRepository.getChatUsersByUserId(user.getId());
        Map<Long, ChatRoom> roomMap = new HashMap<>();
        for (ChatUser chatUser : chatUsers) {
            ChatRoom chatRoom = chatUser.getChatRoom();
            roomMap.put(chatRoom.getId(), chatRoom);
        }
        log.info("chatUsers.size()= {}", chatUsers.size());


        // 각 채팅방들의 최근 메시지를 갖고 온다.  이거 N+1 생기는 거 같은데...
        List<ChatMessage> mostRecentMessages = new ArrayList<>();
        for (ChatUser chatUser : chatUsers) {
            // 나가기한 채팅방 필터링.
            if(chatUser.isLeave()){continue;}
            ChatRoom chatRoom = chatUser.getChatRoom();
            ChatMessage mostRecentMessage = chatMessageRepository.getMostRecentMessage(chatRoom.getId());

            mostRecentMessages.add(mostRecentMessage);
        }
        mostRecentMessages.sort(Comparator.comparing(ChatMessage::getCreatedAt).reversed());

        // key: roomId
        Map<Long, ChatUser> chatUserMap = new HashMap<>();
        for (ChatUser chatUser : chatUsers) {
            ChatRoom chatRoom = chatUser.getChatRoom();
            chatUserMap.put(chatRoom.getId(), chatUser);
        }

        // newMessage 유무.
        List<ChatRoomPreviewResponse> responses = new ArrayList<>();
        for (ChatMessage mostRecentMessage : mostRecentMessages) {
            ChatRoom chatRoom = roomMap.get(mostRecentMessage.getChatRoom().getId());
            ChatUser chatUser = chatUserMap.get(chatRoom.getId());

            boolean isThereNewMessage = mostRecentMessage.getCreatedAt().isAfter(chatUser.getLastAccessedAt());
            if(mostRecentMessage.getMessageType()==MessageType.ENTER){isThereNewMessage=false;}

            ChatRoomPreviewResponse preview = new ChatRoomPreviewResponse(chatRoom.getId(), "roomName", mostRecentMessage.getMessage(), chatRoom.getType(), isThereNewMessage);
            responses.add(preview);
        }


        return responses;
    }

    // 세션 처리해줘야 함.
    // 구독 취소 후 나가기.
    public void leaveRoom(UserDomain user, Long roomId) {
        log.info("[ChatRoomService][leaveRoom]");
        // 개인 채팅방.
        List<ChatUser> chatUsers = chatUserRepository.getChatUsers(roomId);

        log.info("chatUsers.size()= {}", chatUsers.size());

        ChatUser chatUser = null;
        ChatUser targetUser = null;

        for (ChatUser chatUser1 : chatUsers) {
            if(Objects.equals(chatUser1.getUser().getId(), user.getId())){chatUser = chatUser1;}
            else{targetUser = chatUser1;}
        }

        if(chatUser==null){throw new ChatRoomException(ChatRoomErrorCode.CANNOT_LEAVE_CHAT_ROOM_AGAIN);}


        ChatRoom room = chatUser.getChatRoom();


        if(room.getType()==ChatRoomType.GROUP){
            log.info("그룹 채팅방은 나갈 수 없음.");
            throw new ChatRoomException(ChatRoomErrorCode.CANNOT_LEAVE_CHAT_ROOM);
        }

        chatUser.leaveRoom();
        ChatUserSession.offRoom(user.getId(), roomId);

        // 상대방 나갔어.
        if(Objects.requireNonNull(targetUser).isLeave()){
            // 방 폭파.

            // chatUsers 삭제.
            chatUserRepository.deleteChatUsersByRoomId(roomId);

            // 메시지 삭제
            chatMessageRepository.deleteMessagesByRoomId(roomId);

            // 방 삭제
            chatRoomRepository.deleteChatRoom(roomId);

        }
    }
}
