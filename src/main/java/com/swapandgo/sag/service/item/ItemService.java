package com.swapandgo.sag.service.item;

import com.swapandgo.sag.domain.item.Item;
import com.swapandgo.sag.domain.item.ItemType;
import com.swapandgo.sag.domain.user.User;
import com.swapandgo.sag.dto.item.ItemRequest;
import com.swapandgo.sag.repository.ItemRepository;
import com.swapandgo.sag.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public Long saveUsedItem(Long userId, ItemRequest request){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
        );

        if (request.getDeposit() != null){
            throw new IllegalArgumentException("중고 물품은 보증금 설정이 불가합니다.");
        }

        Item item = Item.create(
                user,
                request.getTitle(),
                request.getContent(),
                request.getPrice(),
                null,
                ItemType.RESALE,
                request.getTradeType(),
                request.getCategory(),
                request.getLocation(),
                request.getImages()
        );

        itemRepository.save(item);

        return item.getId();
    }

    public Long saveRentalItem(Long userId, ItemRequest request){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
        );
        Item item = Item.create(
                user,
                request.getTitle(),
                request.getContent(),
                request.getPrice(),
                request.getDeposit(),
                ItemType.RENTAL,
                request.getTradeType(),
                request.getCategory(),
                request.getLocation(),
                request.getImages()
        );

        itemRepository.save(item);

        return item.getId();
    }

    public Long updateUsedItem(Long userId, Long itemId, ItemRequest request){
        Item findItem = itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("아이템을 찾을 수 없습니다 : " + itemId));
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다 : " + userId)
        );

        if (!findItem.isOwner(user))
            throw new IllegalArgumentException("게시글을 수정할 권한이 없습니다.");

        if (request.getDeposit() != null){
            throw new IllegalArgumentException("중고 물품은 보증금 설정이 불가합니다.");
        }
        findItem.update(
                request.getTitle(),
                request.getContent(),
                request.getPrice(),
                null,
                request.getCategory(),
                request.getTradeType()
        );

        return findItem.getId();
    }

    public Long updateRentalItem(Long userId, Long itemId, ItemRequest request){
        Item findItem = itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("아이템을 찾을 수 없습니다 : " + itemId));
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다 : " + userId));
        if (!findItem.isOwner(user))
            throw new IllegalArgumentException("게시글을 수정할 권한이 없습니다.");

        findItem.update(
                request.getTitle(),
                request.getContent(),
                request.getPrice(),
                request.getDeposit(),
                request.getCategory(),
                request.getTradeType()
        );

        return findItem.getId();
    }

    public void deleteItem(Long userId, Long itemId){
        Item findItem = itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("아이템을 찾을 수 없습니다 : " + itemId));
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다 : " + userId)
        );

        if (!findItem.isOwner(user))
            throw new IllegalArgumentException("게시글을 삭제할 권한이 없습니다.");

        itemRepository.delete(findItem);
    }

}
