package com.swapandgo.sag.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swapandgo.sag.domain.item.Item;
import com.swapandgo.sag.domain.item.ItemStatus;
import com.swapandgo.sag.domain.item.ItemType;
import com.swapandgo.sag.domain.QImage;
import com.swapandgo.sag.domain.item.QItem;
import com.swapandgo.sag.dto.search.SearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemQueryRepository {
    private final JPAQueryFactory queryFactory;

    // 기본 Search page
    public List<Item> usedItemSearch(SearchRequest request, ItemType itemType){
        QItem item = QItem.item;
        BooleanBuilder builder = new BooleanBuilder();

        // 1. region
        if(hasText(request.getRegion())) {
            builder.and(item.location.eq(request.getRegion()));
        }

        // 2. category
        if(request.getCategory() != null && !request.getCategory().isEmpty()){
            builder.and(item.category.in(request.getCategory()));
        }

        // 3. 판매 여부 true면 판매중인 상품만
        if(request.isAvailable()){
            builder.and(item.status.eq(ItemStatus.ACTIVE));
        }

        // 4. 삽니다 팝니다
        builder.and(item.tradeType.eq(request.getDealType()));

        // 5. 검색어
        if (hasText(request.getKeyword())){
            builder.and(item.title.containsIgnoreCase(request.getKeyword()));
            //content에도 검색어 일치 확인 할건지?
        }

        // 6. 가격 범위
        if (request.getMinPrice() != null){
            builder.and(item.price.goe(request.getMinPrice()));
        }
        if (request.getMaxPrice() != null){
            builder.and(item.price.loe(request.getMaxPrice()));
        }

        // 7. 커서
        if(request.getCursor() != null && request.getCursor() > 0){
            builder.and(item.id.lt(request.getCursor()));
        }

        // 8. 아이템 타입
        builder.and(item.type.eq(itemType));

        return queryFactory
                .selectFrom(item)
                .where(builder)
                .orderBy(item.id.desc()) // 최신순
                .limit(request.getLimit()+1) // hasNext 확인용으로 + 1
                .fetch();

    }

    //사용자의 최근 게시물들, 중고와 대여 최대 9개씩
    public List<Item> findRecentPost(int resaleLimit, int rentalLimit, Long itemId){
        QItem item = QItem.item;
        QImage image = QImage.image;

        List<Item> resaleItems = queryFactory.selectFrom(item)
                .distinct()
                .leftJoin(item.images, image).fetchJoin()
                .where(item.type.eq(ItemType.RESALE),
                        item.id.ne(itemId))
                .orderBy(item.createdAt.desc())
                .limit(resaleLimit)
                .fetch();

        List<Item> rentalItems = queryFactory.selectFrom(item)
                .distinct()
                .leftJoin(item.images, image).fetchJoin()
                .where(item.type.eq(ItemType.RENTAL),
                        item.id.ne(itemId))
                .orderBy(item.createdAt.desc())
                .limit(rentalLimit)
                .fetch();

        List<Item> result = new ArrayList<>();
        result.addAll(resaleItems);
        result.addAll(rentalItems);

        return result;

    }

    private boolean hasText(String str){
        return str != null && !str.isBlank();
    }
}
