package com.swapandgo.sag.dto.search;

import com.swapandgo.sag.domain.item.Category;
import com.swapandgo.sag.domain.item.TradeType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SearchRequest {
    private String region;
    private List<Category> category;
    private boolean isAvailable;
    private TradeType dealType;
    private String keyword;
    private Integer minPrice;
    private Integer maxPrice;
    private Long cursor;
    private Integer limit;
}
