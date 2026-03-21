package com.swapandgo.sag.api.tradeoffer;

import com.swapandgo.sag.domain.item.ItemType;
import com.swapandgo.sag.domain.tradeoffer.TradeOfferStatus;
import com.swapandgo.sag.dto.tradeoffer.TradeOfferCreateRequest;
import com.swapandgo.sag.dto.tradeoffer.TradeOfferResponse;
import com.swapandgo.sag.dto.tradeoffer.TradeOfferSimpleResponse;
import com.swapandgo.sag.dto.tradeoffer.TradeOfferStatusUpdateRequest;
import com.swapandgo.sag.security.user.CustomUserDetails;
import com.swapandgo.sag.service.tradeoffer.TradeOfferAcceptResult;
import com.swapandgo.sag.service.tradeoffer.TradeOfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class TradeOfferController {
    private final TradeOfferService tradeOfferService;

    @PostMapping("/api/tradeoffers/resale")
    public ResponseEntity<TradeOfferSimpleResponse> createResaleTradeOffer(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid TradeOfferCreateRequest request) {
        Long userId = userDetails.getUserId();
        TradeOfferSimpleResponse response = new TradeOfferSimpleResponse(
                tradeOfferService.createOffer(userId, request, ItemType.RESALE)
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/tradeoffers/rental")
    public ResponseEntity<TradeOfferSimpleResponse> createRentalTradeOffer(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid TradeOfferCreateRequest request) {
        Long userId = userDetails.getUserId();
        TradeOfferSimpleResponse response = new TradeOfferSimpleResponse(
                tradeOfferService.createOffer(userId, request, ItemType.RENTAL)
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/api/tradeoffers/{tradeOfferId}")
    public ResponseEntity<TradeOfferSimpleResponse> updateTradeOfferStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long tradeOfferId,
            @RequestBody @Valid TradeOfferStatusUpdateRequest request) {
        Long userId = userDetails.getUserId();
        TradeOfferStatus status = request.getStatus();
        if (status == TradeOfferStatus.CANCELED) {
            TradeOfferSimpleResponse response = new TradeOfferSimpleResponse(
                    tradeOfferService.cancelOffer(userId, tradeOfferId)
            );
            return ResponseEntity.ok(response);
        }
        if (status == TradeOfferStatus.ACCEPTED) {
            TradeOfferAcceptResult result = tradeOfferService.acceptOffer(userId, tradeOfferId);
            TradeOfferSimpleResponse response = new TradeOfferSimpleResponse(
                    result.getTradeOffer(), result.getTransactionId()
            );
            return ResponseEntity.ok(response);
        }
        if (status == TradeOfferStatus.REJECTED) {
            TradeOfferSimpleResponse response = new TradeOfferSimpleResponse(
                    tradeOfferService.rejectOffer(userId, tradeOfferId)
            );
            return ResponseEntity.ok(response);
        }
        throw new IllegalArgumentException("지원하지 않는 상태 변경입니다.");
    }

    @GetMapping("/api/tradeoffers")
    public ResponseEntity<List<TradeOfferResponse>> listTradeOffers(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("role") String role) {
        Long userId = userDetails.getUserId();
        List<TradeOfferResponse> responses;
        if ("sender".equalsIgnoreCase(role)) {
            responses = tradeOfferService.listSentOffers(userId).stream()
                    .map(TradeOfferResponse::new)
                    .collect(Collectors.toList());
        } else if ("receiver".equalsIgnoreCase(role)) {
            responses = tradeOfferService.listReceivedOffers(userId).stream()
                    .map(TradeOfferResponse::new)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("role은 sender 또는 receiver 여야 합니다.");
        }
        return ResponseEntity.ok(responses);
    }
}
