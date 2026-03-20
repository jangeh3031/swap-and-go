package com.swapandgo.sag.service.transaction;

import com.swapandgo.sag.domain.item.Item;
import com.swapandgo.sag.domain.transaction.EarlyReturnStatus;
import com.swapandgo.sag.domain.transaction.Transaction;
import com.swapandgo.sag.dto.transaction.EarlyReturnRequest;
import com.swapandgo.sag.dto.transaction.TransactionResponse;
import com.swapandgo.sag.repository.TransactionRepository;
import com.swapandgo.sag.repository.UserRepository;
import com.swapandgo.sag.repository.WishListRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final WishListRepository wishListRepository;

    public Transaction requestEarlyReturn(Long requesterId, Long transactionId, EarlyReturnRequest request) {
        Transaction transaction = findTransaction(transactionId);
        if (!transaction.getBuyer().getId().equals(requesterId)) {
            throw new IllegalArgumentException("조기 반납 요청 권한이 없습니다.");
        }

        transaction.requestEarlyReturn(request.getNewEndAt(), LocalDateTime.now());
        return transaction;
    }

    public Transaction cancelEarlyReturn(Long requesterId, Long transactionId) {
        Transaction transaction = findTransaction(transactionId);
        if (!transaction.getBuyer().getId().equals(requesterId)) {
            throw new IllegalArgumentException("조기 반납 취소 권한이 없습니다.");
        }

        transaction.cancelEarlyReturn(LocalDateTime.now());
        return transaction;
    }

    public Transaction acceptEarlyReturn(Long ownerId, Long transactionId) {
        Transaction transaction = findTransaction(transactionId);
        Item item = transaction.getItem();
        if (!item.getUser().getId().equals(ownerId)) {
            throw new IllegalArgumentException("조기 반납 수락 권한이 없습니다.");
        }

        transaction.acceptEarlyReturn(LocalDateTime.now());
        return transaction;
    }

    public Transaction rejectEarlyReturn(Long ownerId, Long transactionId) {
        Transaction transaction = findTransaction(transactionId);
        Item item = transaction.getItem();
        if (!item.getUser().getId().equals(ownerId)) {
            throw new IllegalArgumentException("조기 반납 거절 권한이 없습니다.");
        }

        transaction.rejectEarlyReturn(LocalDateTime.now());
        return transaction;
    }

    public Transaction updateEarlyReturnStatus(Long ownerId, Long transactionId, EarlyReturnStatus status) {
        if (status == EarlyReturnStatus.ACCEPTED) {
            return acceptEarlyReturn(ownerId, transactionId);
        }
        if (status == EarlyReturnStatus.REJECTED) {
            return rejectEarlyReturn(ownerId, transactionId);
        }
        if (status == EarlyReturnStatus.CANCELED) {
            return cancelEarlyReturn(ownerId, transactionId);
        }
        throw new IllegalArgumentException("지원하지 않는 상태 변경입니다.");
    }

    @Transactional(readOnly = true)
    public List<Transaction> listBuyerTransactions(Long buyerId) {
        return transactionRepository.findAllByBuyerIdOrderByIdDesc(buyerId);
    }

    @Transactional(readOnly = true)
    public List<Transaction> listSellerTransactions(Long sellerId) {
        return transactionRepository.findAllByItemUserIdOrderByIdDesc(sellerId);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> listBuyerTransactionResponses(Long buyerId) {
        List<Transaction> transactions = listBuyerTransactions(buyerId);
        Set<Long> likedItemIds = findLikedItemIds(buyerId, transactions);
        return transactions.stream()
                .map(t -> new TransactionResponse(t, likedItemIds.contains(t.getItem().getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> listSellerTransactionResponses(Long sellerId) {
        List<Transaction> transactions = listSellerTransactions(sellerId);
        Set<Long> likedItemIds = findLikedItemIds(sellerId, transactions);
        return transactions.stream()
                .map(t -> new TransactionResponse(t, likedItemIds.contains(t.getItem().getId())))
                .collect(Collectors.toList());
    }

    private Transaction findTransaction(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found: " + transactionId));
    }

    private Set<Long> findLikedItemIds(Long userId, List<Transaction> transactions) {
        List<Long> itemIds = transactions.stream()
                .map(t -> t.getItem().getId())
                .distinct()
                .collect(Collectors.toList());
        if (itemIds.isEmpty()) {
            return Set.of();
        }
        return wishListRepository.findLikedItemIdsByUserId(userId, itemIds);
    }
}
