package com.swapandgo.sag.dto.image;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MultipleImageUploadResponse {
    private List<String> imageUrls;
}
