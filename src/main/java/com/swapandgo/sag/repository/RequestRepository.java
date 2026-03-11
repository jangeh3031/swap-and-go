package com.swapandgo.sag.repository;

import com.swapandgo.sag.domain.request.Request;
import com.swapandgo.sag.domain.request.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    boolean existsByItemIdAndRequesterIdAndStatus(Long itemId, Long requesterId, RequestStatus status);

    List<Request> findAllByItemIdAndStatus(Long itemId, RequestStatus status);
}
