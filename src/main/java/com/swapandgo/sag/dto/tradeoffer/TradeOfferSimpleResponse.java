package com.swapandgo.sag.dto.tradeoffer;

import com.swapandgo.sag.domain.tradeoffer.TradeOffer;
import com.swapandgo.sag.domain.tradeoffer.TradeOfferStatus;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({
        "tradeOfferId",
        "transactionId",
        "itemId",
        "requesterId",
        "status",
        "createdAt",
        "startAt",
        "endAt"
})
public class TradeOfferSimpleResponse {
    private final Long tradeOfferId;
    private final Long transactionId;
    private final Long itemId;
    private final Long requesterId;
    private final TradeOfferStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;

    public TradeOfferSimpleResponse(TradeOffer tradeOffer) {
        this.tradeOfferId = tradeOffer.getId();
        this.transactionId = null;
        this.itemId = tradeOffer.getItem().getId();
        this.requesterId = tradeOffer.getRequester().getId();
        this.status = tradeOffer.getStatus();
        this.createdAt = tradeOffer.getCreatedAt();
        this.startAt = tradeOffer.getStartAt();
        this.endAt = tradeOffer.getEndAt();
    }

    public TradeOfferSimpleResponse(TradeOffer tradeOffer, Long transactionId) {
        this.tradeOfferId = tradeOffer.getId();
        this.transactionId = transactionId;
        this.itemId = tradeOffer.getItem().getId();
        this.requesterId = tradeOffer.getRequester().getId();
        this.status = tradeOffer.getStatus();
        this.createdAt = tradeOffer.getCreatedAt();
        this.startAt = tradeOffer.getStartAt();
        this.endAt = tradeOffer.getEndAt();
    }
}
