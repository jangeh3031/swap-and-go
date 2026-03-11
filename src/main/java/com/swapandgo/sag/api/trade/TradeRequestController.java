package com.swapandgo.sag.api.trade;

import com.swapandgo.sag.dto.trade.TradeRequestCreateRequest;
import com.swapandgo.sag.dto.trade.TradeRequestResponse;
import com.swapandgo.sag.security.user.CustomUserDetails;
import com.swapandgo.sag.service.trade.TradeRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trades")
public class TradeRequestController {
    private final TradeRequestService tradeRequestService;

    @PostMapping
    public ResponseEntity<TradeRequestResponse> createTradeRequest(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody TradeRequestCreateRequest request
    ){
        Long userId = userDetails.getUserId();
        TradeRequestResponse response = tradeRequestService.createTradeRequest(userId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{requestId}/accept")
    public ResponseEntity<TradeRequestResponse> acceptTradeRequest(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long requestId
    ){
        Long userId = userDetails.getUserId();
        TradeRequestResponse response = tradeRequestService.acceptTradeRequest(requestId, userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{requestId}/reject")
    public ResponseEntity<TradeRequestResponse> rejectTradeRequest(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long requestId
    ){
        Long userId = userDetails.getUserId();
        TradeRequestResponse response = tradeRequestService.rejectTradeRequest(requestId, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{requestId}")
    public ResponseEntity<TradeRequestResponse> cancelTradeRequest(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long requestId
    ){
        Long userId = userDetails.getUserId();
        TradeRequestResponse response = tradeRequestService.cancelTradeRequest(requestId, userId);
        return ResponseEntity.ok(response);
    }
}
