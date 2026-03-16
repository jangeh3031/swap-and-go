package com.swapandgo.sag.service.tradeoffer;

import com.swapandgo.sag.domain.tradeoffer.TradeOffer;
import lombok.Getter;

@Getter
public class TradeOfferAcceptResult {
    private final TradeOffer tradeOffer;
    private final Long transactionId;

    public TradeOfferAcceptResult(TradeOffer tradeOffer, Long transactionId) {
        this.tradeOffer = tradeOffer;
        this.transactionId = transactionId;
    }
}
