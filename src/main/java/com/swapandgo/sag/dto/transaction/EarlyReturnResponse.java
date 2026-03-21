package com.swapandgo.sag.dto.transaction;

import com.swapandgo.sag.domain.transaction.EarlyReturnStatus;
import com.swapandgo.sag.domain.transaction.Transaction;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({
        "transactionId",
        "itemId",
        "requesterId",
        "status",
        "requestedAt",
        "newEndAt",
        "currentEndAt"
})
public class EarlyReturnResponse {
    private final Long transactionId;
    private final Long itemId;
    private final Long requesterId;
    private final EarlyReturnStatus status;
    private final LocalDateTime requestedAt;
    private final LocalDateTime newEndAt;
    private final LocalDateTime currentEndAt;

    public EarlyReturnResponse(Transaction transaction) {
        this.transactionId = transaction.getId();
        this.itemId = transaction.getItem().getId();
        this.requesterId = transaction.getBuyer().getId();
        this.status = transaction.getEarlyReturnStatus();
        this.requestedAt = transaction.getEarlyReturnRequestedAt();
        this.newEndAt = transaction.getEarlyReturnAt();
        this.currentEndAt = transaction.getEndAt();
    }
}
