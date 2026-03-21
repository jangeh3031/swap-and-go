package com.swapandgo.sag.repository;

import com.swapandgo.sag.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByUserIdOrderByIdDesc(Long userId);
}
