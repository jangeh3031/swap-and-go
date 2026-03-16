package com.swapandgo.sag.repository;

import com.swapandgo.sag.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t " +
            "WHERE t.item.id = :itemId " +
            "AND t.startAt <= :now " +
            "AND  t.endAt >= :now")
    Optional<Transaction> findCurrentRentalByItemId(@Param("itemId") Long itemId,
                                                    @Param("now")LocalDateTime now);

    List<Transaction> findAllByBuyerIdOrderByIdDesc(Long buyerId);

    List<Transaction> findAllByItemUserIdOrderByIdDesc(Long sellerId);
}
