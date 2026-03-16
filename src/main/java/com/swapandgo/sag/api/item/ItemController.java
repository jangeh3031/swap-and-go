package com.swapandgo.sag.api.item;

import com.swapandgo.sag.dto.item.ItemRequest;
import com.swapandgo.sag.dto.item.ItemResponse;
import com.swapandgo.sag.security.user.CustomUserDetails;
import com.swapandgo.sag.service.item.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ItemResponse> createItem(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart("data") @Valid ItemRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
            ){
        Long userId = userDetails.getUserId();
        Long itemId = itemService.createItem(userId, request, images);
        ItemResponse response = new ItemResponse(itemId, "중고거래 물품이 등록 되었습니다.");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemResponse> updateItem(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("itemId") Long itemId,
            @RequestBody ItemRequest request
            ){
        Long userId = userDetails.getUserId();

        Long updateItemId = itemService.updateItem(userId, itemId, request);
        ItemResponse response = new ItemResponse(updateItemId, "중고 거래 물품 정보가 수정되었습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> deleteItem(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("itemId") Long itemId
    ){
        Long userId = userDetails.getUserId();
        itemService.deleteItem(userId, itemId);
        return ResponseEntity.ok("게시글이 성공적으로 삭제되었습니다.");
    }

}
