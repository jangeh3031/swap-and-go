package com.swapandgo.sag.dto.transaction;

import com.swapandgo.sag.domain.item.ItemType;
import com.swapandgo.sag.domain.transaction.EarlyReturnStatus;
import com.swapandgo.sag.domain.transaction.Transaction;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TransactionResponse {
    private final Long transactionId;
    private final Long itemId;
    private final Long buyerId;
    private final Long sellerId;
    private final ItemType itemType;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    private final EarlyReturnStatus earlyReturnStatus;
    private final LocalDateTime createdAt;

    public TransactionResponse(Transaction transaction) {
        this.transactionId = transaction.getId();
        this.itemId = transaction.getItem().getId();
        this.buyerId = transaction.getBuyer().getId();
        this.sellerId = transaction.getItem().getUser().getId();
        this.itemType = transaction.getType();
        this.startAt = transaction.getStartAt();
        this.endAt = transaction.getEndAt();
        this.earlyReturnStatus = transaction.getEarlyReturnStatus();
        this.createdAt = transaction.getCreatedAt();
    }
}
