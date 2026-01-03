package com.swapandgo.sag.api.wishList;

import com.swapandgo.sag.dto.wishList.WishListResponse;
import com.swapandgo.sag.security.user.CustomUserDetails;
import com.swapandgo.sag.service.wishList.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WishListController {
    private final WishListService wishListService;

    @PostMapping("/api/items/{itemId}/favorites")
    public ResponseEntity<WishListResponse> addWishList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long itemId){
        Long userId = userDetails.getUserId();
        WishListResponse wishListResponse = wishListService.toggleWishList(itemId, userId);
        return ResponseEntity.ok(wishListResponse);
    }

    @DeleteMapping("/api/items/{itemId}/favorites")
    public ResponseEntity<WishListResponse> deleteWishList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long itemId){
        Long userId = userDetails.getUserId();
        WishListResponse wishListResponse = wishListService.toggleWishList(itemId, userId);
        return ResponseEntity.ok(wishListResponse);
    }
}
