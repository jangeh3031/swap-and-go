package com.swapandgo.sag.dto.search;

import com.swapandgo.sag.domain.item.Category;
import com.swapandgo.sag.domain.item.TradeType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class ItemSearchDto {
    private Long itemId;
    private String title;
    private BigDecimal price;
    private BigDecimal deposit;
    private String region;
    private TradeType dealType;
    private Category category;
    private Boolean status;
    private Boolean isLiked;
    private String thumbnailUrl;
    private LocalDateTime createdAt;
}
