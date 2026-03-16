package com.swapandgo.sag.api.tradeoffer;

import com.swapandgo.sag.dto.tradeoffer.TradeOfferCreateRequest;
import com.swapandgo.sag.dto.tradeoffer.TradeOfferResponse;
import com.swapandgo.sag.security.user.CustomUserDetails;
import com.swapandgo.sag.service.tradeoffer.TradeOfferAcceptResult;
import com.swapandgo.sag.service.tradeoffer.TradeOfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class TradeOfferController {
    private final TradeOfferService tradeOfferService;

    @PostMapping("/api/tradeoffer")
    public ResponseEntity<TradeOfferResponse> createTradeOffer(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid TradeOfferCreateRequest request) {
        Long userId = userDetails.getUserId();
        TradeOfferResponse response = new TradeOfferResponse(
                tradeOfferService.createOffer(userId, request)
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/tradeoffer/{tradeOfferId}")
    public ResponseEntity<TradeOfferResponse> cancelTradeOffer(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long tradeOfferId) {
        Long userId = userDetails.getUserId();
        TradeOfferResponse response = new TradeOfferResponse(
                tradeOfferService.cancelOffer(userId, tradeOfferId)
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/tradeoffer/{tradeOfferId}/accept")
    public ResponseEntity<TradeOfferResponse> acceptTradeOffer(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long tradeOfferId) {
        Long userId = userDetails.getUserId();
        TradeOfferAcceptResult result = tradeOfferService.acceptOffer(userId, tradeOfferId);
        TradeOfferResponse response = new TradeOfferResponse(result.getTradeOffer(), result.getTransactionId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/tradeoffer/{tradeOfferId}/reject")
    public ResponseEntity<TradeOfferResponse> rejectTradeOffer(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long tradeOfferId) {
        Long userId = userDetails.getUserId();
        TradeOfferResponse response = new TradeOfferResponse(
                tradeOfferService.rejectOffer(userId, tradeOfferId)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/tradeoffer/sent")
    public ResponseEntity<List<TradeOfferResponse>> getSentTradeOffers(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        List<TradeOfferResponse> responses = tradeOfferService.listSentOffers(userId).stream()
                .map(TradeOfferResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/api/tradeoffer/received")
    public ResponseEntity<List<TradeOfferResponse>> getReceivedTradeOffers(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        List<TradeOfferResponse> responses = tradeOfferService.listReceivedOffers(userId).stream()
                .map(TradeOfferResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}
