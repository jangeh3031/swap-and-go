package com.swapandgo.sag.dto.search.detail;

import com.swapandgo.sag.domain.item.Category;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class RentalDetailResponse {
    private Long itemId;
    private String title;
    private String content;
    private BigDecimal deposit;
    private BigDecimal price;
    private String region;
    private Category category;
    private Boolean isMine;
    private Boolean isLiked;
    private Boolean isAvailable;
    private LocalDateTime createdAt;
    private List<String> images;
    private RentalInfo rentalInfo;
    private SellerDto seller;
    private List<RecentPostsBySeller> recentPostsBySeller;
}
