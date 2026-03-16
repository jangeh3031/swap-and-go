package com.swapandgo.sag.api.auth;

import com.swapandgo.sag.domain.user.Address;
import com.swapandgo.sag.domain.user.User;
import com.swapandgo.sag.dto.auth.UserResponse;
import com.swapandgo.sag.dto.user.SimpleAddress;
import com.swapandgo.sag.security.user.CustomUserDetails;
import com.swapandgo.sag.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserAuthController {
    private final UserService userService;


    //DTO 변환 책임 넘기기? 나중에
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal CustomUserDetails userDetails){
        User user = userService.getUserByEmail(userDetails.getEmail());
        log.info("controller 처리");
        SimpleAddress simpleAddress = null;
        Address address = user.getAddress();
        if(address != null){
            simpleAddress = new SimpleAddress(
                    address.getCountry(),
                    address.getRegion(),
                    address.getStreet()
            );
        }

        UserResponse userResponse = new UserResponse(
                user.getEmail(),
                user.getUsername(),
                simpleAddress
        );
        return ResponseEntity.ok(userResponse);

    }
}
