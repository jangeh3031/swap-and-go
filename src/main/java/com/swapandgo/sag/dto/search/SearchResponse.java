package com.swapandgo.sag.dto.search;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SearchResponse {
    private Integer count;
    private Integer nextCursor;
    private Boolean hasNext;
    private List<ItemSearchDto> items;

}
