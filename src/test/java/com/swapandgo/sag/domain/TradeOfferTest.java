package com.swapandgo.sag.domain;

import com.swapandgo.sag.domain.item.Category;
import com.swapandgo.sag.domain.item.Item;
import com.swapandgo.sag.domain.item.ItemType;
import com.swapandgo.sag.domain.item.TradeType;
import com.swapandgo.sag.domain.tradeoffer.TradeOffer;
import com.swapandgo.sag.domain.tradeoffer.TradeOfferStatus;
import com.swapandgo.sag.domain.user.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TradeOfferTest {

    @Test
    void create_rejectsInvalidPeriod() {
        User requester = User.createUser("r", "r@test.com", "pw", null);
        Item item = Item.create(
                User.createUser("o", "o@test.com", "pw", null),
                "t", "c", new BigDecimal("10.00"), null,
                ItemType.RESALE, TradeType.SELL, Category.전자기기, "Seoul", List.of()
        );

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.minusDays(1);

        assertThatThrownBy(() -> TradeOffer.create(requester, item, start, end))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("종료 시간");
    }

    @Test
    void statusTransitions_onlyFromPending() {
        User requester = User.createUser("r", "r@test.com", "pw", null);
        Item item = Item.create(
                User.createUser("o", "o@test.com", "pw", null),
                "t", "c", new BigDecimal("10.00"), null,
                ItemType.RESALE, TradeType.SELL, Category.전자기기, "Seoul", List.of()
        );

        TradeOffer offer = TradeOffer.create(requester, item, null, null);
        offer.accept();
        assertThat(offer.getStatus()).isEqualTo(TradeOfferStatus.ACCEPTED);

        assertThatThrownBy(offer::reject)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("진행 중인 요청");
    }

    @Test
    void cancel_thenReject_throws() {
        User requester = User.createUser("r", "r@test.com", "pw", null);
        Item item = Item.create(
                User.createUser("o", "o@test.com", "pw", null),
                "t", "c", new BigDecimal("10.00"), null,
                ItemType.RESALE, TradeType.SELL, Category.티켓, "Seoul", List.of()
        );

        TradeOffer offer = TradeOffer.create(requester, item, null, null);
        offer.cancel();
        assertThat(offer.getStatus()).isEqualTo(TradeOfferStatus.CANCELED);

        assertThatThrownBy(offer::reject)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("진행 중인 요청");
    }

    @Test
    void create_allowsSameStartAndEnd() {
        User requester = User.createUser("r", "r@test.com", "pw", null);
        Item item = Item.create(
                User.createUser("o", "o@test.com", "pw", null),
                "t", "c", new BigDecimal("10.00"), new BigDecimal("5.00"),
                ItemType.RENTAL, TradeType.SELL, Category.스포츠, "Seoul", List.of()
        );

        LocalDateTime at = LocalDateTime.now();
        TradeOffer offer = TradeOffer.create(requester, item, at, at);

        assertThat(offer.getStartAt()).isEqualTo(at);
        assertThat(offer.getEndAt()).isEqualTo(at);
        assertThat(offer.getStatus()).isEqualTo(TradeOfferStatus.PENDING);
    }
}
