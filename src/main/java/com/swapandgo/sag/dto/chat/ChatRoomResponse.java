package com.swapandgo.sag.dto.chat;

import com.swapandgo.sag.domain.chat.ChatRoom;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatRoomResponse {
    private final Long roomId;
    private final Long itemId;
    private final String itemTitle;
    private final String itemThumbnailUrl;
    private final Long buyerId;
    private final String buyerName;
    private final Long sellerId;
    private final String sellerName;
    private final String lastMessage;
    private final LocalDateTime lastMessageAt;
    private final LocalDateTime createdAt;

    public ChatRoomResponse(ChatRoom room) {
        this.roomId = room.getId();
        this.itemId = room.getItem().getId();
        this.itemTitle = room.getItem().getTitle();
        this.itemThumbnailUrl = room.getItem().getThumbnailUrl();
        this.buyerId = room.getBuyer().getId();
        this.buyerName = room.getBuyer().getUsername();
        this.sellerId = room.getSeller().getId();
        this.sellerName = room.getSeller().getUsername();
        this.lastMessage = null;
        this.lastMessageAt = null;
        this.createdAt = room.getCreatedAt();
    }

    public ChatRoomResponse(ChatRoom room, String lastMessage, LocalDateTime lastMessageAt) {
        this.roomId = room.getId();
        this.itemId = room.getItem().getId();
        this.itemTitle = room.getItem().getTitle();
        this.itemThumbnailUrl = room.getItem().getThumbnailUrl();
        this.buyerId = room.getBuyer().getId();
        this.buyerName = room.getBuyer().getUsername();
        this.sellerId = room.getSeller().getId();
        this.sellerName = room.getSeller().getUsername();
        this.lastMessage = lastMessage;
        this.lastMessageAt = lastMessageAt;
        this.createdAt = room.getCreatedAt();
    }
}
