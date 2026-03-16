package com.swapandgo.sag.domain.tradeoffer;

import com.swapandgo.sag.domain.item.Item;
import com.swapandgo.sag.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "trade_offer")
public class TradeOffer {
    @Id
    @GeneratedValue
    @Column(name = "trade_offer_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Enumerated(EnumType.STRING)
    private TradeOfferStatus status;

    private LocalDateTime startAt;
    private LocalDateTime endAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TradeOffer create(User requester, Item item,
                                    LocalDateTime startAt, LocalDateTime endAt) {
        if (startAt != null && endAt != null && endAt.isBefore(startAt)) {
            throw new IllegalArgumentException("대여 종료 시간은 시작 시간보다 빠를 수 없습니다.");
        }

        TradeOffer tradeOffer = new TradeOffer();
        tradeOffer.requester = requester;
        tradeOffer.item = item;
        tradeOffer.status = TradeOfferStatus.PENDING;
        tradeOffer.startAt = startAt;
        tradeOffer.endAt = endAt;
        tradeOffer.createdAt = LocalDateTime.now();
        tradeOffer.updatedAt = LocalDateTime.now();
        return tradeOffer;
    }

    public void cancel() {
        requirePending();
        this.status = TradeOfferStatus.CANCELED;
        this.updatedAt = LocalDateTime.now();
    }

    public void accept() {
        requirePending();
        this.status = TradeOfferStatus.ACCEPTED;
        this.updatedAt = LocalDateTime.now();
    }

    public void reject() {
        requirePending();
        this.status = TradeOfferStatus.REJECTED;
        this.updatedAt = LocalDateTime.now();
    }

    private void requirePending() {
        if (this.status != TradeOfferStatus.PENDING) {
            throw new IllegalStateException("진행 중인 요청만 처리할 수 있습니다.");
        }
    }
}
