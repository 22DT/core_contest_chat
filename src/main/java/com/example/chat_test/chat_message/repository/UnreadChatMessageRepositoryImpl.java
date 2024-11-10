package com.example.chat_test.chat_message.repository;

/*@Repository
@RequiredArgsConstructor
@Slf4j
public class UnreadChatMessageRepositoryImpl implements UnreadChatMessageRepository {
    private final UnreadChatMessageJpaRepository unreadChatMessageJpaRepository;
    private final ChatUserJpaRepository chatUserJpaRepository;
    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private final ChatRoomJpaRepository chatRoomJpaRepository;


    @Override
    public void save(List<Long> offlineUserIds, Long chatMessageId, Long chatRoomId) {
        log.info("[UnreadChatMessageRepositoryImpl][save]");

        ChatRoom chatRoom = chatRoomJpaRepository.getReferenceById(chatRoomId);
        ChatMessage chatMessage = chatMessageJpaRepository.getReferenceById(chatMessageId);
        List<UnreadChatMessage> unreadChatMessages=new ArrayList<>();

        for (Long offlineUserId : offlineUserIds) {
            ChatUser chatUser = chatUserJpaRepository.getReferenceById(offlineUserId);

            UnreadChatMessage unreadChatMessage = UnreadChatMessage.builder()
                    .chatUser(chatUser)
                    .chatRoom(chatRoom)
                    .chatMessage(chatMessage)
                    .build();

            unreadChatMessages.add(unreadChatMessage);
        }

        unreadChatMessageJpaRepository.saveAll(unreadChatMessages);
    }
}*/
