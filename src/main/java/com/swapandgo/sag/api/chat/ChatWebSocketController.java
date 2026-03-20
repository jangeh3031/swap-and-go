package com.swapandgo.sag.api.chat;

import com.swapandgo.sag.dto.chat.ChatMessageResponse;
import com.swapandgo.sag.dto.chat.ChatMessageSendRequest;
import com.swapandgo.sag.security.user.CustomUserDetails;
import com.swapandgo.sag.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/send")
    public void sendMessage(ChatMessageSendRequest request, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new SecurityException("인증이 필요합니다.");
        }
        Long userId = userDetails.getUserId();
        ChatMessageResponse response = new ChatMessageResponse(
                chatService.sendMessage(userId, request)
        );
        String destination = "/sub/chat/rooms/" + request.getRoomId();
        messagingTemplate.convertAndSend(destination, response);
    }
}
