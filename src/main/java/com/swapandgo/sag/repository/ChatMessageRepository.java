package com.swapandgo.sag.repository;

import com.swapandgo.sag.domain.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByChatRoomIdOrderByIdAsc(Long chatRoomId);
    Optional<ChatMessage> findTopByChatRoomIdOrderByIdDesc(Long chatRoomId);
}
