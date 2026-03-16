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

    @Enumerated(EnumType.STRING)
    private EarlyReturnStatus earlyReturnStatus;

    private LocalDateTime earlyReturnAt;
    private LocalDateTime earlyReturnRequestedAt;

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
        transaction.earlyReturnStatus = EarlyReturnStatus.NONE;
        transaction.createdAt = LocalDateTime.now();
        transaction.updatedAt = LocalDateTime.now();

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

    // 대여 조기 반납 요청
    public void requestEarlyReturn(LocalDateTime newEndAt, LocalDateTime now){
        requireRental();
        requireActiveRental(now);
        if (this.earlyReturnStatus == null) {
            this.earlyReturnStatus = EarlyReturnStatus.NONE;
        }
        if (this.earlyReturnStatus == EarlyReturnStatus.PENDING) {
            throw new IllegalStateException("이미 조기 반납 요청이 진행 중입니다.");
        }
        checkEndTime(newEndAt);
        if (newEndAt.isBefore(now)) {
            throw new IllegalArgumentException("조기 반납 시간은 현재 시간보다 빠를 수 없습니다.");
        }
        this.earlyReturnAt = newEndAt;
        this.earlyReturnRequestedAt = now;
        this.earlyReturnStatus = EarlyReturnStatus.PENDING;
        this.updatedAt = now;
    }

    public void cancelEarlyReturn(LocalDateTime now){
        if (this.earlyReturnStatus != EarlyReturnStatus.PENDING) {
            throw new IllegalStateException("진행 중인 조기 반납 요청만 취소할 수 있습니다.");
        }
        this.earlyReturnStatus = EarlyReturnStatus.CANCELED;
        this.updatedAt = now;
    }

    public void acceptEarlyReturn(LocalDateTime now){
        if (this.earlyReturnStatus != EarlyReturnStatus.PENDING) {
            throw new IllegalStateException("진행 중인 조기 반납 요청만 수락할 수 있습니다.");
        }
        if (this.earlyReturnAt == null) {
            throw new IllegalStateException("조기 반납 시간이 없습니다.");
        }
        checkEndTime(this.earlyReturnAt);
        this.endAt = this.earlyReturnAt;
        this.earlyReturnStatus = EarlyReturnStatus.ACCEPTED;
        this.updatedAt = now;
    }

    public void rejectEarlyReturn(LocalDateTime now){
        if (this.earlyReturnStatus != EarlyReturnStatus.PENDING) {
            throw new IllegalStateException("진행 중인 조기 반납 요청만 거절할 수 있습니다.");
        }
        this.earlyReturnStatus = EarlyReturnStatus.REJECTED;
        this.updatedAt = now;
    }

    private void requireRental() {
        if (this.type != ItemType.RENTAL) {
            throw new IllegalStateException("대여 거래만 조기 반납 요청이 가능합니다.");
        }
    }

    private void requireActiveRental(LocalDateTime now) {
        if (this.startAt == null || this.endAt == null) {
            throw new IllegalStateException("대여 기간이 설정되지 않았습니다.");
        }
        if (now.isBefore(this.startAt)) {
            throw new IllegalStateException("대여 시작 전에는 조기 반납 요청이 불가합니다.");
        }
        if (now.isAfter(this.endAt)) {
            throw new IllegalStateException("이미 종료된 대여입니다.");
        }
    }
}
