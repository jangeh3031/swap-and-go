package com.swapandgo.sag.service.transaction;

import com.swapandgo.sag.domain.item.Item;
import com.swapandgo.sag.domain.transaction.Transaction;
import com.swapandgo.sag.dto.transaction.EarlyReturnRequest;
import com.swapandgo.sag.repository.TransactionRepository;
import com.swapandgo.sag.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

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

    @Transactional(readOnly = true)
    public List<Transaction> listBuyerTransactions(Long buyerId) {
        return transactionRepository.findAllByBuyerIdOrderByIdDesc(buyerId);
    }

    @Transactional(readOnly = true)
    public List<Transaction> listSellerTransactions(Long sellerId) {
        return transactionRepository.findAllByItemUserIdOrderByIdDesc(sellerId);
    }

    private Transaction findTransaction(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found: " + transactionId));
    }
}
