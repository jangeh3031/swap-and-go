package com.swapandgo.sag.api.mypage;

import com.swapandgo.sag.dto.mypage.MyItemResponse;
import com.swapandgo.sag.dto.mypage.MyProfileResponse;
import com.swapandgo.sag.dto.mypage.MyProfileUpdateRequest;
import com.swapandgo.sag.dto.mypage.MyAccountDeleteRequest;
import com.swapandgo.sag.dto.wishList.WishListItemResponse;
import com.swapandgo.sag.security.user.CustomUserDetails;
import com.swapandgo.sag.service.mypage.MyPageService;
import com.swapandgo.sag.service.wishList.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {
    private final WishListService wishListService;
    private final MyPageService myPageService;

    @GetMapping("/profile")
    public ResponseEntity<MyProfileResponse> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(myPageService.getMyProfile(userId));
    }

    @PatchMapping("/profile")
    public ResponseEntity<MyProfileResponse> updateMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MyProfileUpdateRequest request) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(myPageService.updateMyProfile(userId, request));
    }

    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MyAccountDeleteRequest request) {
        Long userId = userDetails.getUserId();
        myPageService.deleteMyAccount(userId, request);
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }

    @GetMapping("/items")
    public ResponseEntity<List<MyItemResponse>> listMyItems(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        List<MyItemResponse> responses = myPageService.listMyItems(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/wishlist")
    public ResponseEntity<List<WishListItemResponse>> listMyWishList(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        List<WishListItemResponse> responses = wishListService.listMyWishList(userId);
        return ResponseEntity.ok(responses);
    }
}
