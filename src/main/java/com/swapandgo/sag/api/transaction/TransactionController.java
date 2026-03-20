package com.swapandgo.sag.api.transaction;

import com.swapandgo.sag.dto.transaction.EarlyReturnRequest;
import com.swapandgo.sag.dto.transaction.EarlyReturnResponse;
import com.swapandgo.sag.dto.transaction.EarlyReturnStatusUpdateRequest;
import com.swapandgo.sag.dto.transaction.TransactionResponse;
import com.swapandgo.sag.security.user.CustomUserDetails;
import com.swapandgo.sag.service.transaction.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/api/transactions/{transactionId}/early-return")
    public ResponseEntity<EarlyReturnResponse> requestEarlyReturn(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long transactionId,
            @RequestBody @Valid EarlyReturnRequest request) {
        Long userId = userDetails.getUserId();
        EarlyReturnResponse response = new EarlyReturnResponse(
                transactionService.requestEarlyReturn(userId, transactionId, request)
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/api/transactions/{transactionId}/early-return")
    public ResponseEntity<EarlyReturnResponse> updateEarlyReturnStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long transactionId,
            @RequestBody @Valid EarlyReturnStatusUpdateRequest request) {
        Long userId = userDetails.getUserId();
        EarlyReturnResponse response = new EarlyReturnResponse(
                transactionService.updateEarlyReturnStatus(userId, transactionId, request.getStatus())
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/transactions/buyer")
    public ResponseEntity<List<TransactionResponse>> getBuyerTransactions(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        List<TransactionResponse> responses = transactionService.listBuyerTransactionResponses(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/api/transactions/seller")
    public ResponseEntity<List<TransactionResponse>> getSellerTransactions(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        List<TransactionResponse> responses = transactionService.listSellerTransactionResponses(userId);
        return ResponseEntity.ok(responses);
    }
}
