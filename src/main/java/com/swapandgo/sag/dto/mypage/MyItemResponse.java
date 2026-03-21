package com.swapandgo.sag.dto.mypage;

import com.swapandgo.sag.domain.item.Item;
import com.swapandgo.sag.domain.item.ItemStatus;
import com.swapandgo.sag.domain.item.ItemType;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class MyItemResponse {
    private final Long itemId;
    private final String title;
    private final BigDecimal price;
    private final String location;
    private final String thumbnailUrl;
    private final ItemStatus status;
    private final ItemType itemType;
    private final LocalDateTime createdAt;

    public MyItemResponse(Item item) {
        this.itemId = item.getId();
        this.title = item.getTitle();
        this.price = item.getPrice();
        this.location = item.getLocation();
        this.thumbnailUrl = item.getThumbnailUrl();
        this.status = item.getStatus();
        this.itemType = item.getType();
        this.createdAt = item.getCreatedAt();
    }
}
