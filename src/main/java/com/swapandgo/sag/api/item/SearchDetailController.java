package com.swapandgo.sag.api.item;

import com.swapandgo.sag.dto.search.detail.RentalDetailResponse;
import com.swapandgo.sag.dto.search.detail.ResaleDetailResponse;
import com.swapandgo.sag.security.user.CustomUserDetails;
import com.swapandgo.sag.service.item.ItemDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SearchDetailController {
    private final ItemDetailService itemDetailService;

    @GetMapping("resale/items/{itemId}")
    public ResponseEntity<ResaleDetailResponse> resaleItemDetail(
            @PathVariable("itemId") Long itemId,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ){
        Long userId = userDetails != null ? userDetails.getUserId() : null;
        ResaleDetailResponse resaleDetailResponse = itemDetailService.resaleItemDetailPage(itemId, userId);
        return ResponseEntity.ok(resaleDetailResponse);
    }

    @GetMapping("rental/items/{itemId}")
    public ResponseEntity<RentalDetailResponse> rentalItemDetail(
            @PathVariable("itemId") Long itemId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        Long userId = userDetails != null ? userDetails.getUserId() : null;
        RentalDetailResponse rentalDetailResponse = itemDetailService.rentalItemDetailPage(itemId, userId);
        return ResponseEntity.ok(rentalDetailResponse);
    }


}
