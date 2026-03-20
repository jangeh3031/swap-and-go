package com.swapandgo.sag.api.chat;

import com.swapandgo.sag.dto.chat.ChatMessageResponse;
import com.swapandgo.sag.dto.chat.ChatMessageSendRequest;
import com.swapandgo.sag.dto.chat.ChatRoomCreateRequest;
import com.swapandgo.sag.dto.chat.ChatRoomResponse;
import com.swapandgo.sag.security.user.CustomUserDetails;
import com.swapandgo.sag.service.chat.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/api/chat/rooms")
    public ResponseEntity<ChatRoomResponse> createRoom(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid ChatRoomCreateRequest request) {
        Long userId = userDetails.getUserId();
        ChatRoomResponse response = new ChatRoomResponse(
                chatService.createOrGetRoom(request.getItemId(), userId)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/chat/rooms")
    public ResponseEntity<List<ChatRoomResponse>> listMyRooms(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        List<ChatRoomResponse> responses = chatService.listMyRoomResponses(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/api/chat/rooms/{roomId}/messages")
    public ResponseEntity<List<ChatMessageResponse>> listMessages(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long roomId) {
        Long userId = userDetails.getUserId();
        List<ChatMessageResponse> responses = chatService.listMessages(roomId, userId).stream()
                .map(ChatMessageResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/api/chat/rooms/{roomId}/messages")
    public ResponseEntity<ChatMessageResponse> sendMessage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long roomId,
            @RequestBody @Valid ChatMessageSendRequest request) {
        Long userId = userDetails.getUserId();
        // Path와 body roomId 일치 검증
        if (!roomId.equals(request.getRoomId())) {
            throw new IllegalArgumentException("roomId가 일치하지 않습니다.");
        }
        ChatMessageResponse response = new ChatMessageResponse(
                chatService.sendMessage(userId, request)
        );
        return ResponseEntity.ok(response);
    }
}
