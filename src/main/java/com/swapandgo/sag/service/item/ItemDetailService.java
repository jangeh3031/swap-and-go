package com.swapandgo.sag.service.item;

import com.swapandgo.sag.domain.Image;
import com.swapandgo.sag.domain.item.Item;
import com.swapandgo.sag.domain.item.ItemStatus;
import com.swapandgo.sag.domain.item.ItemType;
import com.swapandgo.sag.domain.transaction.Transaction;
import com.swapandgo.sag.domain.user.User;
import com.swapandgo.sag.dto.search.detail.*;
import com.swapandgo.sag.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class ItemDetailService {
    private final ItemRepository itemRepository;
    private final ItemQueryRepository itemQueryRepository;
    private final UserRepository userRepository;
    private final WishListRepository wishListRepository;
    private final TransactionRepository transactionRepository;
    public ResaleDetailResponse resaleItemDetailPage(Long itemId, Long userId){
        //item정보들
        //itemId, title, content, price, region, category, createdAt
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("해당 id의 item이 존재하지 않습니다."));
        if (item.getType() == ItemType.RENTAL)
            throw new IllegalArgumentException("해당 id의 item은 대여상품입니다.");

        //isAvailable
        boolean isAvailable = item.getStatus() != ItemStatus.COMPLETED;


        //isMine
        //isLiked
        boolean isMine = false;
        boolean isLiked = false;
        if (userId != null){
            isMine = userId.equals(item.getUser().getId());
            isLiked = wishListRepository.existsByItemIdAndUserId(itemId, userId);
        }

        //images String으로 전부
        List<String> images = new ArrayList<>();
        List<Image> imageList = item.getImages();

        for(Image image : imageList){
            images.add(image.getUrl());
        }

        //sellerId username 받기
        User user = userRepository.findById(item.getUser().getId()).orElseThrow(
                ()-> new IllegalArgumentException("user를 찾을 수 없습니다.")
        );
        SellerDto sellerDto = new SellerDto(user.getId(), user.getUsername());

        //recentPostBySeller 전체
        //item의 userId가 일치하는 사람의 최근에 올린 게시물 9개 9개
        //현재 게시물은 넣지 않음
        List<Item> recentPosts = itemQueryRepository.findRecentPost(9, 9, itemId);
        //isLiked
        List<Long> itemIds = new ArrayList<>();
        for (Item getItem: recentPosts){
            Long id = getItem.getId();
            itemIds.add(id);
        }
        Set<Long> likedItemIds = (userId != null)
                ? wishListRepository.findLikedItemIdsByUserId(userId, itemIds) : Set.of();  // 비로그인 시 빈 Set
        List<RecentPostsBySeller> recentItemDtos = new ArrayList<>();

        for (Item recentPost : recentPosts){
            boolean recentPostIsLiked = likedItemIds.contains(recentPost.getId());
            RecentPostsBySeller dto = itemToDto(recentPost, recentPostIsLiked);
            recentItemDtos.add(dto);
        }

        return ResaleDetailResponse.builder()
                .itemId(itemId)
                .title(item.getTitle())
                .content(item.getContent())
                .price(item.getPrice())
                .region(item.getLocation())
                .category(item.getCategory())
                .isMine(isMine)
                .isLiked(isLiked)
                .isAvailable(isAvailable)
                .createdAt(item.getCreatedAt())
                .images(images)
                .seller(sellerDto)
                .recentPostsBySeller(recentItemDtos)
                .build();
    }

    public RentalDetailResponse rentalItemDetailPage(Long itemId, Long userId){
        //item정보들
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("해당 id의 item이 존재하지 않습니다."));
        if (item.getType() == ItemType.RESALE)
            throw new IllegalArgumentException("해당 id의 item은 중고 상품입니다.");

        //isAvailable
        boolean isAvailable = item.getStatus() != ItemStatus.RENTED;

        boolean isMine = false;
        boolean isLiked = false;
        if (userId != null){
            isMine = userId.equals(item.getUser().getId());
            isLiked = wishListRepository.existsByItemIdAndUserId(itemId, userId);
        }

        List<String> images = new ArrayList<>();
        List<Image> imageList = item.getImages();
        for (Image image : imageList){
            images.add(image.getUrl());
        }

        //rentalInfo
        Optional<Transaction> currentRental = transactionRepository.findCurrentRentalByItemId(itemId, LocalDateTime.now());
        RentalInfo rentalInfo;
        if (currentRental.isPresent()){
            Transaction transaction = currentRental.get();
            rentalInfo = new RentalInfo(true, transaction.getStartAt(), transaction.getEndAt());
        }else {
            rentalInfo = new RentalInfo(false, null, null);
        }


        User user = userRepository.findById(item.getUser().getId()).orElseThrow(
                ()-> new IllegalArgumentException("user를 찾을 수 없습니다.")
        );
        SellerDto sellerDto = new SellerDto(user.getId(), user.getUsername());



        //recentPostBySeller
        List<Item> recentPosts = itemQueryRepository.findRecentPost(9, 9, itemId);
        //isLiked
        List<Long> itemIds = new ArrayList<>();
        for (Item getItem: recentPosts){
            Long id = getItem.getId();
            itemIds.add(id);
        }
        Set<Long> likedItemIds = (userId != null)
                ? wishListRepository.findLikedItemIdsByUserId(userId, itemIds) : Set.of();  // 비로그인 시 빈 Set
        List<RecentPostsBySeller> recentItemDtos = new ArrayList<>();

        for (Item recentPost : recentPosts){
            boolean recentPostIsLiked = likedItemIds.contains(recentPost.getId());
            RecentPostsBySeller dto = itemToDto(recentPost, recentPostIsLiked);
            recentItemDtos.add(dto);
        }

        return RentalDetailResponse.builder()
                .itemId(itemId)
                .title(item.getTitle())
                .content(item.getContent())
                .deposit(item.getDeposit())
                .price(item.getPrice())
                .region(item.getLocation())
                .category(item.getCategory())
                .isMine(isMine)
                .isLiked(isLiked)
                .isAvailable(isAvailable)
                .createdAt(item.getCreatedAt())
                .images(images)
                .rentalInfo(rentalInfo)
                .seller(sellerDto)
                .recentPostsBySeller(recentItemDtos)
                .build();


    }

    private RecentPostsBySeller itemToDto(Item item, boolean isLiked){
        boolean isAvailable;
        isAvailable = item.getStatus() == ItemStatus.ACTIVE;
        return RecentPostsBySeller.builder()
                .itemId(item.getId())
                .title(item.getTitle())
                .price(item.getPrice())
                .deposit(item.getDeposit())
                .region(item.getLocation())
                .dealType(item.getTradeType())
                .category(item.getCategory())
                .isAvailable(isAvailable)
                .isLiked(isLiked)
                .thumbnailUrl(item.getThumbnailUrl()) // rf
                .createdAt(item.getCreatedAt())
                .itemType(item.getType())
                .build();
    }
}
