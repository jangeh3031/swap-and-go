package com.swapandgo.sag.dto.search.detail;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swapandgo.sag.domain.item.Category;
import com.swapandgo.sag.domain.item.ItemType;
import com.swapandgo.sag.domain.item.TradeType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class RecentPostsBySeller {
    private Long itemId;
    private String title;
    private BigDecimal deposit;
    private BigDecimal price;
    private String region;
    private TradeType dealType;
    private Category category;
    private Boolean isAvailable;
    private Boolean isLiked;
    private String thumbnailUrl;
    private LocalDateTime createdAt;
    private ItemType itemType;
}
