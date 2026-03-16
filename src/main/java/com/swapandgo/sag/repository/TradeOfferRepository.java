package com.swapandgo.sag.repository;

import com.swapandgo.sag.domain.tradeoffer.TradeOffer;
import com.swapandgo.sag.domain.tradeoffer.TradeOfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeOfferRepository extends JpaRepository<TradeOffer, Long> {
    boolean existsByItemIdAndRequesterIdAndStatus(Long itemId, Long requesterId, TradeOfferStatus status);

    List<TradeOffer> findAllByItemIdAndStatus(Long itemId, TradeOfferStatus status);

    List<TradeOffer> findAllByRequesterIdOrderByIdDesc(Long requesterId);

    List<TradeOffer> findAllByItemUserIdOrderByIdDesc(Long ownerId);
}
