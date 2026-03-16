package com.swapandgo.sag.api.item;

import com.swapandgo.sag.domain.item.Category;
import com.swapandgo.sag.domain.item.ItemType;
import com.swapandgo.sag.domain.item.TradeType;
import com.swapandgo.sag.domain.user.User;
import com.swapandgo.sag.dto.search.SearchRequest;
import com.swapandgo.sag.dto.search.SearchResponse;
import com.swapandgo.sag.security.user.CustomUserDetails;
import com.swapandgo.sag.service.item.ItemSearchService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {
    private final ItemSearchService searchService;

    @GetMapping("resale/items/search")
    public ResponseEntity<SearchResponse> searchResaleItems(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "false") boolean isAvailable,
            @RequestParam(defaultValue = "SELL") TradeType dealType,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String priceRange,
            @RequestParam(defaultValue = "0") Long cursor,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ){

        PriceRange parsedPriceRange = parsePriceRange(priceRange);

        List<Category> categories = null;
        if (category != null && !category.isEmpty()){
            categories = Arrays.stream(category.split(","))
                    .map(String::trim)
                    .map(Category::valueOf)
                    .collect(Collectors.toList());
        }

        SearchRequest request = SearchRequest.builder()
                .region(region)
                .category(categories)
                .isAvailable(isAvailable)
                .dealType(dealType)
                .keyword(keyword)
                .minPrice(parsedPriceRange.getMin())
                .maxPrice(parsedPriceRange.getMax())
                .cursor(cursor)
                .limit(12)
                .build();

        //로그인 안된 유저도 검색할 수 있음
        Long userId = userDetails != null ? userDetails.getUserId() : null;
        SearchResponse response = searchService.search(request, userId, ItemType.RESALE);
        return ResponseEntity.ok(response);

    }

    @GetMapping("rental/items/search")
    public ResponseEntity<SearchResponse> searchRentalItems(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "false") boolean isAvailable,
            @RequestParam(defaultValue = "SELL") TradeType dealType,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String priceRange,
            @RequestParam(defaultValue = "0") Long cursor,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        PriceRange parsedPriceRange = parsePriceRange(priceRange);

        List<Category> categories = null;
        if (category != null && !category.isEmpty()){
            categories = Arrays.stream(category.split(","))
                    .map(String::trim)
                    .map(Category::valueOf)
                    .collect(Collectors.toList());
        }

        SearchRequest request = SearchRequest.builder()
                .region(region)
                .category(categories)
                .isAvailable(isAvailable)
                .dealType(dealType)
                .keyword(keyword)
                .minPrice(parsedPriceRange.getMin())
                .maxPrice(parsedPriceRange.getMax())
                .cursor(cursor)
                .limit(12)
                .build();

        Long userId = userDetails != null ? userDetails.getUserId() : null;
        SearchResponse response = searchService.search(request, userId, ItemType.RENTAL);
        return ResponseEntity.ok(response);

    }

    private PriceRange parsePriceRange(String priceRange){
        if(priceRange == null || priceRange.isBlank()){
            return new PriceRange(null, null);
        }

        String[] parts = priceRange.split("-");
        if(parts.length != 2){
            throw new IllegalArgumentException("Invalid priceRange format Expected : 'min-max'");
        }
        try{
            Integer min = Integer.parseInt(parts[0].trim());
            Integer max = Integer.parseInt(parts[1].trim());
            return new PriceRange(min, max);
        }catch (NumberFormatException e){
            throw new IllegalArgumentException("Invalid priceRange values", e);
        }
    }

    @Getter
    @AllArgsConstructor
    private static class PriceRange{
        private Integer min;
        private Integer max;
    }
}
