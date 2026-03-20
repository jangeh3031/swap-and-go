package com.swapandgo.sag.dto.tradeoffer;

import com.swapandgo.sag.domain.tradeoffer.TradeOffer;
import com.swapandgo.sag.domain.tradeoffer.TradeOfferStatus;
import com.swapandgo.sag.domain.item.ItemType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({
        "tradeOfferId",
        "transactionId",
        "itemId",
        "itemType",
        "itemTitle",
        "requesterId",
        "requesterName",
        "ownerId",
        "ownerName",
        "status",
        "createdAt",
        "startAt",
        "endAt"
})
public class TradeOfferResponse {
    private final Long tradeOfferId;
    private final Long transactionId;
    private final Long itemId;
    private final ItemType itemType;
    private final String itemTitle;
    private final Long requesterId;
    private final String requesterName;
    private final Long ownerId;
    private final String ownerName;
    private final TradeOfferStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;

    public TradeOfferResponse(TradeOffer tradeOffer) {
        this.tradeOfferId = tradeOffer.getId();
        this.transactionId = null;
        this.itemId = tradeOffer.getItem().getId();
        this.itemType = tradeOffer.getItem().getType();
        this.itemTitle = tradeOffer.getItem().getTitle();
        this.requesterId = tradeOffer.getRequester().getId();
        this.requesterName = tradeOffer.getRequester().getUsername();
        this.ownerId = tradeOffer.getItem().getUser().getId();
        this.ownerName = tradeOffer.getItem().getUser().getUsername();
        this.status = tradeOffer.getStatus();
        this.createdAt = tradeOffer.getCreatedAt();
        this.startAt = tradeOffer.getStartAt();
        this.endAt = tradeOffer.getEndAt();
    }

    public TradeOfferResponse(TradeOffer tradeOffer, Long transactionId) {
        this.tradeOfferId = tradeOffer.getId();
        this.transactionId = transactionId;
        this.itemId = tradeOffer.getItem().getId();
        this.itemType = tradeOffer.getItem().getType();
        this.itemTitle = tradeOffer.getItem().getTitle();
        this.requesterId = tradeOffer.getRequester().getId();
        this.requesterName = tradeOffer.getRequester().getUsername();
        this.ownerId = tradeOffer.getItem().getUser().getId();
        this.ownerName = tradeOffer.getItem().getUser().getUsername();
        this.status = tradeOffer.getStatus();
        this.createdAt = tradeOffer.getCreatedAt();
        this.startAt = tradeOffer.getStartAt();
        this.endAt = tradeOffer.getEndAt();
    }
}
