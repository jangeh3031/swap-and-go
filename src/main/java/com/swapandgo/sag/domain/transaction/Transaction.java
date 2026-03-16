package com.swapandgo.sag.domain.transaction;

import com.swapandgo.sag.domain.Image;
import com.swapandgo.sag.domain.item.Item;
import com.swapandgo.sag.domain.item.ItemType;
import com.swapandgo.sag.domain.item.TradeType;
import com.swapandgo.sag.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Transaction {
    @Id @GeneratedValue
    @Column(name = "transaction_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Enumerated(EnumType.STRING)
    private ItemType type;

    private LocalDateTime startAt;
    private LocalDateTime endAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Transaction create(
            User buyer, Item item, ItemType type,
            LocalDateTime startAt, LocalDateTime endAt
    ){
        Transaction transaction = new Transaction();
        transaction.buyer = buyer;
        transaction.item = item;
        transaction.type = type;
        transaction.startAt = startAt;
        transaction.endAt = endAt;

        return transaction;
    }

    //대여의 경의 조기 반납 요청을 처리
    public void updateEndTime(LocalDateTime newEndAt){
        checkEndTime(newEndAt);
        this.endAt = newEndAt;
        this.updatedAt = LocalDateTime.now();
    }

    //새로운 대여 마감시간 요청 시간의 유효성 검사
    public void checkEndTime(LocalDateTime endAt){
        if (endAt == null)
            throw new IllegalArgumentException("새로운 반납 시간은 null일 수 없습니다.");
        if (this.startAt == null || this.endAt == null)
            throw new IllegalStateException("대여 시작/종료 시간이 설정되지 않은 거래입니다.");
        if (endAt.isBefore(this.startAt))
            throw new IllegalArgumentException("새로운 반납 시간은 대여 시작 시간보다 빠를 수 없습니다.");
        if (endAt.isAfter(this.endAt))
            throw new IllegalArgumentException("새로운 반납 시간은 기존 반납 시간보다 늦을 수 없습니다.");
        if (endAt.isEqual(this.endAt))
            throw new IllegalArgumentException("기존 반납 시간과 동일합니다.");
    }
}
