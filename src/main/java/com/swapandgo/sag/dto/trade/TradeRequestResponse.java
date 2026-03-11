package com.swapandgo.sag.dto.trade;

import com.swapandgo.sag.domain.request.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TradeRequestResponse {
    private Long requestId;
    private Long itemId;
    private RequestStatus status;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
