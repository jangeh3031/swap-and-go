package com.swapandgo.sag.repository;

import com.swapandgo.sag.domain.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface WishListRepository extends JpaRepository<WishList, Long> {

    @Query("SELECT w.item.id FROM WishList w " +
    "WHERE w.user.id = :userId AND w.item.id IN :itemIds")
    Set<Long> findLikedItemIdsByUserId(
            @Param("userId") Long userId,
            @Param("itemIds") List<Long> itemIds
            );

    Optional<WishList> findWishListByItemIdAndUserId(Long itemId, Long userId);
}
