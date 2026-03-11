package com.swapandgo.sag.service.trade;

import com.swapandgo.sag.domain.item.Item;
import com.swapandgo.sag.domain.item.ItemStatus;
import com.swapandgo.sag.domain.item.ItemType;
import com.swapandgo.sag.domain.request.Request;
import com.swapandgo.sag.domain.request.RequestStatus;
import com.swapandgo.sag.domain.transaction.Transaction;
import com.swapandgo.sag.domain.user.User;
import com.swapandgo.sag.dto.trade.TradeRequestCreateRequest;
import com.swapandgo.sag.dto.trade.TradeRequestResponse;
import com.swapandgo.sag.repository.ItemRepository;
import com.swapandgo.sag.repository.RequestRepository;
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
public class TradeRequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final TransactionRepository transactionRepository;

    public TradeRequestResponse createTradeRequest(Long userId, TradeRequestCreateRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        Long itemId = request.getItemId();
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found: " + itemId));

        if (item.getStatus() != ItemStatus.ACTIVE){
            throw new IllegalStateException("거래 가능한 상태가 아닙니다.");
        }

        if (requestRepository.existsByItemIdAndRequesterIdAndStatus(itemId, userId, RequestStatus.PENDING)){
            throw new IllegalStateException("이미 대기중인 요청이 있습니다.");
        }

        if (item.getType() == ItemType.RENTAL){
            LocalDateTime startAt = request.getStartAt();
            LocalDateTime endAt = request.getEndAt();
            if (startAt == null || endAt == null){
                throw new IllegalArgumentException("대여 요청에는 대여 기간이 필요합니다.");
            }
            if (!startAt.isBefore(endAt)){
                throw new IllegalArgumentException("대여 시작 시간이 종료 시간보다 이전이어야 합니다.");
            }
        }

        Request requestEntity = item.addRequestForm(user, request.getStartAt(), request.getEndAt());
        requestRepository.save(requestEntity);

        return toResponse(requestEntity);
    }

    public TradeRequestResponse acceptTradeRequest(Long requestId, Long ownerId){
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found: " + requestId));
        Item item = request.getItem();
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + ownerId));

        if (!item.isOwner(owner)){
            throw new SecurityException("요청 수락 권한이 없습니다.");
        }
        if (item.getStatus() != ItemStatus.ACTIVE){
            throw new IllegalStateException("거래 가능한 상태가 아닙니다.");
        }

        request.accept();
        item.completed();

        Transaction transaction = Transaction.create(
                request.getRequester(),
                item,
                item.getType(),
                request.getStartAt(),
                request.getEndAt()
        );
        transactionRepository.save(transaction);

        List<Request> pendingRequests = requestRepository.findAllByItemIdAndStatus(item.getId(), RequestStatus.PENDING);
        for (Request other : pendingRequests){
            if (!other.getId().equals(request.getId())){
                other.reject();
            }
        }

        return toResponse(request);
    }

    public TradeRequestResponse rejectTradeRequest(Long requestId, Long ownerId){
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found: " + requestId));
        Item item = request.getItem();
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + ownerId));

        if (!item.isOwner(owner)){
            throw new SecurityException("요청 거절 권한이 없습니다.");
        }

        request.reject();
        return toResponse(request);
    }

    public TradeRequestResponse cancelTradeRequest(Long requestId, Long requesterId){
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found: " + requestId));
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + requesterId));

        request.cancelByRequester(requester);
        return toResponse(request);
    }

    private TradeRequestResponse toResponse(Request request){
        return new TradeRequestResponse(
                request.getId(),
                request.getItem().getId(),
                request.getStatus(),
                request.getStartAt(),
                request.getEndAt()
        );
    }
}
