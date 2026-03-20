package com.swapandgo.sag.domain.chat;

import com.swapandgo.sag.domain.item.Item;
import com.swapandgo.sag.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class ChatRoom {
    @Id @GeneratedValue
    @Column(name = "chatroom_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> messages = new ArrayList<>();

    private LocalDateTime createdAt;

    public static ChatRoom create(Item item, User buyer, User seller) {
        if (item == null || buyer == null || seller == null) {
            throw new IllegalArgumentException("item/buyer/seller는 필수입니다.");
        }
        ChatRoom room = new ChatRoom();
        room.item = item;
        room.buyer = buyer;
        room.seller = seller;
        room.createdAt = LocalDateTime.now();
        return room;
    }

    public void addMessage(ChatMessage m) {
        if (!messages.contains(m)) {
            messages.add(m);
            m.setChatRoom(this);
        }
    }

    public boolean isParticipant(User user) {
        if (user == null) return false;
        Long userId = user.getId();
        return userId != null
                && ((seller != null && userId.equals(seller.getId()))
                || (buyer != null && userId.equals(buyer.getId())));
    }

}
