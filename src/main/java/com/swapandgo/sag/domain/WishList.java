package com.swapandgo.sag.domain;

import com.swapandgo.sag.domain.item.Item;
import com.swapandgo.sag.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class WishList {
    @Id @GeneratedValue
    @Column(name = "wishlist_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Item item;

    public static WishList create(User user, Item item){
        WishList wishList = new WishList();
        wishList.user = user;
        wishList.item = item;
        return wishList;
    }
}
