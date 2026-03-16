package com.swapandgo.sag.dto.wishList;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WishListResponse {
    private Long itemId;
    private boolean isLiked;
}
