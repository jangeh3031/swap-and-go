package com.swapandgo.sag.domain.chat;


import com.swapandgo.sag.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class ChatMessage {
    @Id @GeneratedValue
    @Column(name = "message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    @Column(length = 10000)
    private String content;

    @Column(length = 255)
    private String url;

    private boolean isRead;

    private LocalDateTime createdAt;


    //생성 메서드
    public static ChatMessage text(ChatRoom room, User sender, String content) {
        return create(room, sender, content, null);
    }

    public static ChatMessage create(ChatRoom room, User sender, String content, String url) {
        ChatMessage m = new ChatMessage();
        m.sender = sender;
        m.content = content;
        m.url = url;
        m.createdAt = LocalDateTime.now();
        room.addMessage(m);
        m.validateSendable();
        return m;
    }

    public void validateSendable() {
        if (chatRoom == null || sender == null)
            throw new IllegalArgumentException("chatRoom/sender는 필수입니다.");
        if (!chatRoom.isParticipant(sender))
            throw new SecurityException("채팅방 참여자만 메시지를 보낼 수 있습니다.");
        if ((content == null || content.isBlank()) && (url == null || url.isBlank()))
            throw new IllegalArgumentException("content 또는 url 중 하나는 반드시 있어야 합니다.");
    }

    // ---------- 보조 ----------
    public boolean isSender(User user) {
        return user != null && user.equals(sender);
    }
    //연관 메서드



}
