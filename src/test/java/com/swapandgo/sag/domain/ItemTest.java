package com.swapandgo.sag.domain;

import com.swapandgo.sag.domain.item.Category;
import com.swapandgo.sag.domain.item.Item;
import com.swapandgo.sag.domain.item.ItemStatus;
import com.swapandgo.sag.domain.item.ItemType;
import com.swapandgo.sag.domain.item.TradeType;
import com.swapandgo.sag.domain.user.Address;
import com.swapandgo.sag.domain.user.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ItemTest {

    @Test
    void create_setsMainImageAsThumbnail() {
        User user = User.createUser("u", "u@test.com", "pw", new Address("KR", "Seoul", "street"));
        Item item = Item.create(
                user,
                "title",
                "content",
                new BigDecimal("100.00"),
                null,
                ItemType.RESALE,
                TradeType.SELL,
                Category.전자기기,
                "Seoul",
                List.of("a.jpg", "b.jpg")
        );

        assertThat(item.getImages()).hasSize(2);
        assertThat(item.getImages().get(0).isMain()).isTrue();
        assertThat(item.getThumbnailUrl()).isEqualTo("a.jpg");
    }

    @Test
    void completed_setsStatusByDeposit() {
        User user = User.createUser("u", "u@test.com", "pw", null);
        Item resale = Item.create(
                user, "t", "c", new BigDecimal("10.00"),
                null, ItemType.RESALE, TradeType.SELL, Category.전자기기, "Seoul", List.of()
        );
        resale.completed();
        assertThat(resale.getStatus()).isEqualTo(ItemStatus.COMPLETED);

        Item rental = Item.create(
                user, "t", "c", new BigDecimal("10.00"),
                new BigDecimal("5.00"), ItemType.RENTAL, TradeType.SELL, Category.가구, "Seoul", List.of()
        );
        rental.completed();
        assertThat(rental.getStatus()).isEqualTo(ItemStatus.RENTED);
    }

    @Test
    void addTradeOffer_rejectsSelfRequest() {
        User owner = User.createUser("o", "o@test.com", "pw", null);
        Item item = Item.create(
                owner, "t", "c", new BigDecimal("10.00"),
                null, ItemType.RESALE, TradeType.SELL, Category.미용, "Seoul", List.of()
        );

        assertThatThrownBy(() -> item.addTradeOffer(owner, null, null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("본인 글");
    }

    @Test
    void addTradeOffer_rentalRequiresPeriod() {
        User owner = User.createUser("o", "o@test.com", "pw", null);
        User requester = User.createUser("r", "r@test.com", "pw", null);
        Item item = Item.create(
                owner, "t", "c", new BigDecimal("10.00"),
                new BigDecimal("5.00"), ItemType.RENTAL, TradeType.SELL, Category.남성의류, "Seoul", List.of()
        );

        assertThatThrownBy(() -> item.addTradeOffer(requester, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("대여 기간");

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        assertThat(item.addTradeOffer(requester, start, end)).isNotNull();

        assertThatThrownBy(() -> item.addTradeOffer(requester, start, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("대여 기간");
    }

    @Test
    void getThumbnailUrl_returnsNullWhenNoImages() {
        User user = User.createUser("u", "u@test.com", "pw", null);
        Item item = Item.create(
                user, "t", "c", new BigDecimal("10.00"),
                null, ItemType.RESALE, TradeType.SELL, Category.기타, "Seoul", List.of()
        );

        assertThat(item.getThumbnailUrl()).isNull();
    }

    @Test
    void getThumbnailUrl_returnsFirstWhenNoMainImage() {
        User user = User.createUser("u", "u@test.com", "pw", null);
        Item item = Item.create(
                user, "t", "c", new BigDecimal("10.00"),
                null, ItemType.RESALE, TradeType.SELL, Category.식품, "Seoul", List.of("a.jpg", "b.jpg")
        );

        item.getImages().forEach(image -> image.unmarkAsMain());

        assertThat(item.getThumbnailUrl()).isEqualTo("a.jpg");
    }

    @Test
    void activate_throwsWhenAlreadyActive() {
        User user = User.createUser("u", "u@test.com", "pw", null);
        Item item = Item.create(
                user, "t", "c", new BigDecimal("10.00"),
                null, ItemType.RESALE, TradeType.SELL, Category.전자기기, "Seoul", List.of()
        );

        assertThatThrownBy(item::activate)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 활성화");
    }

    @Test
    void activate_allowsAfterCompletion() {
        User user = User.createUser("u", "u@test.com", "pw", null);
        Item item = Item.create(
                user, "t", "c", new BigDecimal("10.00"),
                null, ItemType.RESALE, TradeType.SELL, Category.가구, "Seoul", List.of()
        );

        item.completed();
        item.activate();

        assertThat(item.getStatus()).isEqualTo(ItemStatus.ACTIVE);
    }

    @Test
    void completed_throwsWhenAlreadyCompleted() {
        User user = User.createUser("u", "u@test.com", "pw", null);
        Item item = Item.create(
                user, "t", "c", new BigDecimal("10.00"),
                null, ItemType.RESALE, TradeType.SELL, Category.미용, "Seoul", List.of()
        );

        item.completed();

        assertThatThrownBy(item::completed)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 완료");
    }

    @Test
    void validate_throwsWhenTitleBlank() {
        User user = User.createUser("u", "u@test.com", "pw", null);
        Item item = Item.create(
                user, " ", "c", new BigDecimal("10.00"),
                null, ItemType.RESALE, TradeType.SELL, Category.전자기기, "Seoul", List.of()
        );

        assertThatThrownBy(item::validate)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("제목");
    }

    @Test
    void validate_throwsWhenContentBlank() {
        User user = User.createUser("u", "u@test.com", "pw", null);
        Item item = Item.create(
                user, "t", " ", new BigDecimal("10.00"),
                null, ItemType.RESALE, TradeType.SELL, Category.전자기기, "Seoul", List.of()
        );

        assertThatThrownBy(item::validate)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("본문");
    }

    @Test
    void validate_throwsWhenPriceZero() {
        User user = User.createUser("u", "u@test.com", "pw", null);
        Item item = Item.create(
                user, "t", "c", BigDecimal.ZERO,
                null, ItemType.RESALE, TradeType.SELL, Category.전자기기, "Seoul", List.of()
        );

        assertThatThrownBy(item::validate)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격");
    }

    @Test
    void validate_throwsWhenPriceNegative() {
        User user = User.createUser("u", "u@test.com", "pw", null);
        Item item = Item.create(
                user, "t", "c", new BigDecimal("-1.00"),
                null, ItemType.RESALE, TradeType.SELL, Category.전자기기, "Seoul", List.of()
        );

        assertThatThrownBy(item::validate)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격");
    }

    @Test
    void validate_rentalRequiresDeposit() {
        User user = User.createUser("u", "u@test.com", "pw", null);
        Item item = Item.create(
                user, "t", "c", new BigDecimal("10.00"),
                null, ItemType.RENTAL, TradeType.SELL, Category.전자기기, "Seoul", List.of()
        );

        assertThatThrownBy(item::validate)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("보증금");
    }

    @Test
    void validate_rentalRequiresPositiveDeposit() {
        User user = User.createUser("u", "u@test.com", "pw", null);
        Item item = Item.create(
                user, "t", "c", new BigDecimal("10.00"),
                BigDecimal.ZERO, ItemType.RENTAL, TradeType.SELL, Category.전자기기, "Seoul", List.of()
        );

        assertThatThrownBy(item::validate)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("보증금");
    }
}
