package com.swapandgo.sag.dto.transaction;

import com.swapandgo.sag.domain.item.ItemType;
import com.swapandgo.sag.domain.item.ItemStatus;
import com.swapandgo.sag.domain.transaction.EarlyReturnStatus;
import com.swapandgo.sag.domain.transaction.Transaction;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TransactionResponse {
    private final Long transactionId;
    private final Long itemId;
    private final String itemTitle;
    private final String itemThumbnailUrl;
    private final java.math.BigDecimal itemPrice;
    private final String itemLocation;
    private final Boolean isLiked;
    private final Long buyerId;
    private final String buyerName;
    private final Long sellerId;
    private final String sellerName;
    private final ItemType itemType;
    private final ItemStatus itemStatus;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    private final LocalDateTime newEndAt;
    private final EarlyReturnStatus earlyReturnStatus;
    private final LocalDateTime createdAt;

    public TransactionResponse(Transaction transaction) {
        this.transactionId = transaction.getId();
        this.itemId = transaction.getItem().getId();
        this.itemTitle = transaction.getItem().getTitle();
        this.itemThumbnailUrl = transaction.getItem().getThumbnailUrl();
        this.itemPrice = transaction.getItem().getPrice();
        this.itemLocation = transaction.getItem().getLocation();
        this.isLiked = null;
        this.buyerId = transaction.getBuyer().getId();
        this.buyerName = transaction.getBuyer().getUsername();
        this.sellerId = transaction.getItem().getUser().getId();
        this.sellerName = transaction.getItem().getUser().getUsername();
        this.itemType = transaction.getType();
        this.itemStatus = transaction.getItem().getStatus();
        this.startAt = transaction.getStartAt();
        this.endAt = transaction.getEndAt();
        this.newEndAt = transaction.getEarlyReturnAt();
        this.earlyReturnStatus = transaction.getEarlyReturnStatus();
        this.createdAt = transaction.getCreatedAt();
    }

    public TransactionResponse(Transaction transaction, boolean isLiked) {
        this.transactionId = transaction.getId();
        this.itemId = transaction.getItem().getId();
        this.itemTitle = transaction.getItem().getTitle();
        this.itemThumbnailUrl = transaction.getItem().getThumbnailUrl();
        this.itemPrice = transaction.getItem().getPrice();
        this.itemLocation = transaction.getItem().getLocation();
        this.isLiked = isLiked;
        this.buyerId = transaction.getBuyer().getId();
        this.buyerName = transaction.getBuyer().getUsername();
        this.sellerId = transaction.getItem().getUser().getId();
        this.sellerName = transaction.getItem().getUser().getUsername();
        this.itemType = transaction.getType();
        this.itemStatus = transaction.getItem().getStatus();
        this.startAt = transaction.getStartAt();
        this.endAt = transaction.getEndAt();
        this.newEndAt = transaction.getEarlyReturnAt();
        this.earlyReturnStatus = transaction.getEarlyReturnStatus();
        this.createdAt = transaction.getCreatedAt();
    }
}
