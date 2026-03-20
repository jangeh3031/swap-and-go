package com.swapandgo.sag.service.tradeoffer;

import com.swapandgo.sag.domain.item.Item;
import com.swapandgo.sag.domain.item.ItemStatus;
import com.swapandgo.sag.domain.item.ItemType;
import com.swapandgo.sag.domain.tradeoffer.TradeOffer;
import com.swapandgo.sag.domain.tradeoffer.TradeOfferStatus;
import com.swapandgo.sag.domain.transaction.Transaction;
import com.swapandgo.sag.domain.user.User;
import com.swapandgo.sag.dto.tradeoffer.TradeOfferCreateRequest;
import com.swapandgo.sag.repository.ItemRepository;
import com.swapandgo.sag.repository.TradeOfferRepository;
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
public class TradeOfferService {
    private final TradeOfferRepository tradeOfferRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public TradeOffer createOffer(Long requesterId, TradeOfferCreateRequest request, ItemType expectedType) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + requesterId));
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("Item not found: " + request.getItemId()));

        if (expectedType != null && item.getType() != expectedType) {
            throw new IllegalArgumentException("요청 타입이 아이템 타입과 일치하지 않습니다.");
        }

        if (item.getStatus() != ItemStatus.ACTIVE) {
            throw new IllegalStateException("비활성화된 게시글에는 요청을 보낼 수 없습니다.");
        }

        if (tradeOfferRepository.existsByItemIdAndRequesterIdAndStatus(
                item.getId(), requesterId, TradeOfferStatus.PENDING)) {
            throw new IllegalStateException("이미 진행 중인 요청이 있습니다.");
        }

        // 현재 진행 중인 대여가 있으면 요청 불가 (대여 상품 기준)
        if (item.getType() == ItemType.RENTAL) {
            transactionRepository.findCurrentRentalByItemId(item.getId(), LocalDateTime.now())
                    .ifPresent(t -> {
                        throw new IllegalStateException("현재 대여 중인 상품입니다.");
                    });
        }

        TradeOffer tradeOffer = item.addTradeOffer(requester, request.getStartAt(), request.getEndAt());
        return tradeOfferRepository.save(tradeOffer);
    }

    public TradeOffer cancelOffer(Long requesterId, Long tradeOfferId) {
        TradeOffer tradeOffer = tradeOfferRepository.findById(tradeOfferId)
                .orElseThrow(() -> new EntityNotFoundException("TradeOffer not found: " + tradeOfferId));

        if (!tradeOffer.getRequester().getId().equals(requesterId)) {
            throw new IllegalArgumentException("요청을 취소할 권한이 없습니다.");
        }

        tradeOffer.cancel();
        return tradeOffer;
    }

    public TradeOfferAcceptResult acceptOffer(Long ownerId, Long tradeOfferId) {
        TradeOffer tradeOffer = tradeOfferRepository.findById(tradeOfferId)
                .orElseThrow(() -> new EntityNotFoundException("TradeOffer not found: " + tradeOfferId));

        Item item = tradeOffer.getItem();
        if (!item.isOwner(userRepository.getReferenceById(ownerId))) {
            throw new IllegalArgumentException("요청을 수락할 권한이 없습니다.");
        }

        if (item.getStatus() != ItemStatus.ACTIVE) {
            throw new IllegalStateException("비활성화된 게시글의 요청은 수락할 수 없습니다.");
        }

        tradeOffer.accept();
        item.completed();

        Transaction transaction = item.getUser().confirmSentTransaction(tradeOffer);
        transactionRepository.save(transaction);

        // 같은 게시글의 다른 진행 중 요청은 거절 처리
        List<TradeOffer> pendingOffers =
                tradeOfferRepository.findAllByItemIdAndStatus(item.getId(), TradeOfferStatus.PENDING);
        for (TradeOffer pending : pendingOffers) {
            if (!pending.getId().equals(tradeOffer.getId())) {
                pending.reject();
            }
        }

        return new TradeOfferAcceptResult(tradeOffer, transaction.getId());
    }

    public TradeOffer rejectOffer(Long ownerId, Long tradeOfferId) {
        TradeOffer tradeOffer = tradeOfferRepository.findById(tradeOfferId)
                .orElseThrow(() -> new EntityNotFoundException("TradeOffer not found: " + tradeOfferId));

        Item item = tradeOffer.getItem();
        if (!item.isOwner(userRepository.getReferenceById(ownerId))) {
            throw new IllegalArgumentException("요청을 거절할 권한이 없습니다.");
        }

        tradeOffer.reject();
        return tradeOffer;
    }

    @Transactional(readOnly = true)
    public List<TradeOffer> listSentOffers(Long requesterId) {
        return tradeOfferRepository.findAllByRequesterIdOrderByIdDesc(requesterId);
    }

    @Transactional(readOnly = true)
    public List<TradeOffer> listReceivedOffers(Long ownerId) {
        return tradeOfferRepository.findAllByItemUserIdOrderByIdDesc(ownerId);
    }
}
