package com.swapandgo.sag.dto.wishList;

import com.swapandgo.sag.domain.WishList;
import com.swapandgo.sag.domain.item.ItemStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class WishListItemResponse {
    private final Long itemId;
    private final String itemTitle;
    private final String itemThumbnailUrl;
    private final BigDecimal itemPrice;
    private final String itemLocation;
    private final ItemStatus itemStatus;
    private final boolean isLiked;
    private final LocalDateTime itemCreatedAt;

    public WishListItemResponse(WishList wishList) {
        this.itemId = wishList.getItem().getId();
        this.itemTitle = wishList.getItem().getTitle();
        this.itemThumbnailUrl = wishList.getItem().getThumbnailUrl();
        this.itemPrice = wishList.getItem().getPrice();
        this.itemLocation = wishList.getItem().getLocation();
        this.itemStatus = wishList.getItem().getStatus();
        this.isLiked = true;
        this.itemCreatedAt = wishList.getItem().getCreatedAt();
    }
}
