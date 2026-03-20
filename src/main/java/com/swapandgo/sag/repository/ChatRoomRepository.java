package com.swapandgo.sag.repository;

import com.swapandgo.sag.domain.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByItemIdAndBuyerId(Long itemId, Long buyerId);

    List<ChatRoom> findAllByBuyerIdOrSellerIdOrderByIdDesc(Long buyerId, Long sellerId);
}
