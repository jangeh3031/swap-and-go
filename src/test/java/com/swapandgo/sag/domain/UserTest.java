package com.swapandgo.sag.domain;

import com.swapandgo.sag.domain.user.Address;
import com.swapandgo.sag.domain.user.User;
import com.swapandgo.sag.domain.item.Category;
import com.swapandgo.sag.domain.item.Item;
import com.swapandgo.sag.domain.item.ItemType;
import com.swapandgo.sag.domain.item.TradeType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    void updateProfile_updatesOnlyProvidedFields() {
        User user = User.createUser("old", "u@test.com", "oldpw", new Address("KR", "Seoul", "street"));

        user.updateProfile("new", null, new Address("KR", "Busan", "street2"));

        assertThat(user.getUsername()).isEqualTo("new");
        assertThat(user.getPassword()).isEqualTo("oldpw");
        assertThat(user.getAddress().getRegion()).isEqualTo("Busan");
    }

    @Test
    void addWish_addsWishListEntry() {
        User user = User.createUser("u", "u@test.com", "pw", null);
        Item item = Item.create(
                User.createUser("o", "o@test.com", "pw", null),
                "t", "c", new BigDecimal("10.00"), null,
                ItemType.RESALE, TradeType.SELL, Category.상품권, "Seoul", List.of()
        );

        user.addWish(item);

        assertThat(user.getWishLists()).hasSize(1);
        assertThat(user.getWishLists().get(0).getItem()).isEqualTo(item);
    }

    @Test
    void updateProfile_nullAddress_overwritesToNull() {
        User user = User.createUser("u", "u@test.com", "pw", new Address("KR", "Seoul", "street"));

        user.updateProfile(null, null, null);

        assertThat(user.getAddress()).isNull();
    }
}
