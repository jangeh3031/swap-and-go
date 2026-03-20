package com.swapandgo.sag.service.chat;

import com.swapandgo.sag.domain.chat.ChatMessage;
import com.swapandgo.sag.domain.chat.ChatRoom;
import com.swapandgo.sag.domain.item.Item;
import com.swapandgo.sag.domain.user.User;
import com.swapandgo.sag.dto.chat.ChatMessageSendRequest;
import com.swapandgo.sag.dto.chat.ChatRoomResponse;
import com.swapandgo.sag.repository.ChatMessageRepository;
import com.swapandgo.sag.repository.ChatRoomRepository;
import com.swapandgo.sag.repository.ItemRepository;
import com.swapandgo.sag.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ChatRoom createOrGetRoom(Long itemId, Long requesterId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found: " + itemId));
        User buyer = userRepository.findById(requesterId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + requesterId));
        User seller = item.getUser();

        if (seller.getId().equals(buyer.getId())) {
            throw new IllegalArgumentException("본인 상품에는 채팅을 생성할 수 없습니다.");
        }

        return chatRoomRepository.findByItemIdAndBuyerId(itemId, buyer.getId())
                .orElseGet(() -> chatRoomRepository.save(ChatRoom.create(item, buyer, seller)));
    }

    @Transactional(readOnly = true)
    public List<ChatRoom> listMyRooms(Long userId) {
        return chatRoomRepository.findAllByBuyerIdOrSellerIdOrderByIdDesc(userId, userId);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomResponse> listMyRoomResponses(Long userId) {
        List<ChatRoom> rooms = listMyRooms(userId);
        return rooms.stream()
                .map(room -> {
                    ChatMessage lastMessage = chatMessageRepository
                            .findTopByChatRoomIdOrderByIdDesc(room.getId())
                            .orElse(null);
                    if (lastMessage == null) {
                        return new ChatRoomResponse(room);
                    }
                    String content = lastMessage.getContent();
                    if ((content == null || content.isBlank()) && lastMessage.getUrl() != null) {
                        content = "[첨부]";
                    }
                    return new ChatRoomResponse(room, content, lastMessage.getCreatedAt());
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> listMessages(Long roomId, Long userId) {
        ChatRoom room = findRoom(roomId);
        User user = userRepository.getReferenceById(userId);
        if (!room.isParticipant(user)) {
            throw new SecurityException("채팅방 접근 권한이 없습니다.");
        }
        return chatMessageRepository.findAllByChatRoomIdOrderByIdAsc(roomId);
    }

    public ChatMessage sendMessage(Long userId, ChatMessageSendRequest request) {
        ChatRoom room = findRoom(request.getRoomId());
        User sender = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        ChatMessage message = ChatMessage.create(room, sender, request.getContent(), request.getUrl());
        return chatMessageRepository.save(message);
    }

    public ChatRoom findRoom(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("ChatRoom not found: " + roomId));
    }
}
