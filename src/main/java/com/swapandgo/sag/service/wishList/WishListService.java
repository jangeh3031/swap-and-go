package com.swapandgo.sag.service.wishList;

import com.swapandgo.sag.domain.WishList;
import com.swapandgo.sag.domain.item.Item;
import com.swapandgo.sag.domain.user.User;
import com.swapandgo.sag.dto.wishList.WishListItemResponse;
import com.swapandgo.sag.dto.wishList.WishListResponse;
import com.swapandgo.sag.repository.ItemRepository;
import com.swapandgo.sag.repository.UserRepository;
import com.swapandgo.sag.repository.WishListRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WishListService {
    private final WishListRepository wishListRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public WishListResponse toggleWishList(Long itemId, Long userId){
        Optional<WishList> findWish = wishListRepository.findWishListByItemIdAndUserId(itemId, userId);
        //    - User는 이미 영속 상태 -> Cascade.ALL 덕분에 wishLists에 추가된 새 WishList도 자동 INSERT, wishListRepository.save() 호출 불필요

        boolean isLiked;

        if (findWish.isPresent()){
            // User 조회 안 함 → 메모리 불일치 문제 없음
            // 이미 user를 조회한 경우, user의 정보를 계속 한 transaction 내부에서 사용해야하는 경우엔 불일치 문제가 발생할 수 있다.
            wishListRepository.delete(findWish.get());
            isLiked = false;
        }else {
            User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found:  " + userId));
            //FK만 필요한 경우 - getReferenceById 사용
            Item item = itemRepository.getReferenceById(itemId);
            user.addWish(item);
            isLiked = true;
        }

        return new WishListResponse(itemId, isLiked);
    }

    @Transactional(readOnly = true)
    public List<WishListItemResponse> listMyWishList(Long userId) {
        return wishListRepository.findAllByUserIdOrderByIdDesc(userId).stream()
                .map(WishListItemResponse::new)
                .collect(Collectors.toList());
    }

}
