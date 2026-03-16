package com.swapandgo.sag.dto.item;

import com.swapandgo.sag.domain.item.Category;
import com.swapandgo.sag.domain.item.ItemType;
import com.swapandgo.sag.domain.item.TradeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
public class ItemRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private Category category;

    @NotNull
    @Positive
    private BigDecimal price;

    private BigDecimal deposit;

    @NotNull
    private TradeType dealType;

    @NotNull
    private ItemType itemType;

    @NotBlank
    private String region;
}
