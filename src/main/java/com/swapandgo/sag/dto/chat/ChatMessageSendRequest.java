package com.swapandgo.sag.dto.chat;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageSendRequest {
    @NotNull
    private Long roomId;

    private String content;
    private String url;
}
