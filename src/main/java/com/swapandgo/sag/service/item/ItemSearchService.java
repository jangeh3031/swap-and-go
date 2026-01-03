package com.swapandgo.sag.service.item;

import com.swapandgo.sag.domain.item.Item;
import com.swapandgo.sag.domain.item.ItemStatus;
import com.swapandgo.sag.domain.item.ItemType;
import com.swapandgo.sag.dto.search.ItemSearchDto;
import com.swapandgo.sag.dto.search.SearchRequest;
import com.swapandgo.sag.dto.search.SearchResponse;
import com.swapandgo.sag.repository.ItemQueryRepository;
import com.swapandgo.sag.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemSearchService {
    private final ItemQueryRepository itemQueryRepository;
    private final WishListRepository wishListRepository;

    //repository에서 조건에 맞는 아이템들을 가져오고, user확인을 통해 내가 like를 눌렀는지 확 후
    //searchResponse 형태로 반환
    public SearchResponse search(SearchRequest request, Long userId, ItemType itemType){
        //최신순으로 limit 만큼 (12개) 가져오기

        List<Item> items = itemQueryRepository.usedItemSearch(request, itemType);

        boolean hasNext = items.size() > request.getLimit();

        if(hasNext){
            items = items.subList(0, request.getLimit());
        }

        List<Long> itemIds = new ArrayList<>();
        for (Item item: items){
            Long id = item.getId();
            itemIds.add(id);
        }

        //검색한 아이템들 수 중에서 현재 사용자가 좋아요를 누른 아이템들의 id를 set으로 반환
        // 로그인한 경우에만 찜 목록 조회
        Set<Long> likedItemIds = (userId != null)
                ? wishListRepository.findLikedItemIdsByUserId(userId, itemIds) : Set.of();  // 비로그인 시 빈 Set
        List<ItemSearchDto> itemDtos = new ArrayList<>();


        for(Item item: items){
            Long itemId = item.getId();
            boolean isLiked = likedItemIds.contains(itemId);
            ItemSearchDto dto = toDto(item, isLiked);
            itemDtos.add(dto);
        }

        //nextCursor 계산
        Integer nextCursor = hasNext && !items.isEmpty()
                ? items.get(items.size() - 1).getId().intValue() : null;

        return SearchResponse.builder()
                .count(itemDtos.size())
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .items(itemDtos)
                .build();

    }

    private ItemSearchDto toDto(Item item, boolean isLiked){
        boolean itemStatus;
        itemStatus = item.getStatus() == ItemStatus.ACTIVE;
        return ItemSearchDto.builder()
                .itemId(item.getId())
                .title(item.getTitle())
                .price(item.getPrice())
                .region(item.getLocation())
                .category(item.getCategory())
                .dealType(item.getTradeType())
                .status(itemStatus)
                .isLiked(isLiked)
                .thumbnailUrl(item.getThumbnailUrl())
                .createdAt(item.getCreatedAt())
                .build();
    }


}
