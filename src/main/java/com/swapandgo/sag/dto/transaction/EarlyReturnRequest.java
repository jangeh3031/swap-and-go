package com.swapandgo.sag.dto.transaction;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class EarlyReturnRequest {
    @NotNull
    private LocalDateTime newEndAt;
}
