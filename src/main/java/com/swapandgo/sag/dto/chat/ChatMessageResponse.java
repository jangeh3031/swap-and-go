package com.swapandgo.sag.dto.chat;

import com.swapandgo.sag.domain.chat.ChatMessage;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatMessageResponse {
    private final Long messageId;
    private final Long roomId;
    private final Long senderId;
    private final String senderName;
    private final String content;
    private final String url;
    private final LocalDateTime createdAt;

    public ChatMessageResponse(ChatMessage message) {
        this.messageId = message.getId();
        this.roomId = message.getChatRoom().getId();
        this.senderId = message.getSender().getId();
        this.senderName = message.getSender().getUsername();
        this.content = message.getContent();
        this.url = message.getUrl();
        this.createdAt = message.getCreatedAt();
    }
}
