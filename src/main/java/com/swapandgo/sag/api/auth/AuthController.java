package com.swapandgo.sag.api.auth;

import com.swapandgo.sag.domain.user.Address;
import com.swapandgo.sag.domain.user.User;
import com.swapandgo.sag.dto.auth.*;
import com.swapandgo.sag.dto.user.SimpleAddress;
import com.swapandgo.sag.repository.UserRepository;
import com.swapandgo.sag.security.user.CustomUserDetails;
import com.swapandgo.sag.service.auth.AuthService;
import com.swapandgo.sag.service.auth.EmailVerificationService;
import com.swapandgo.sag.service.user.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final EmailVerificationService emailVerificationService;
    private final UserService userService;

    //이메일 인증 코드 발송
    @PostMapping("/email")
    public ResponseEntity<String> sendVerificationCode(@RequestBody SendVerificationCodeRequest request){
        emailVerificationService.sendVerificationCode(request.getEmail());
        return ResponseEntity.ok("인증 코드가 발송되었습니다.");
    }
    //인증 코드 확인, 토큰 발급
    @PostMapping("/email-confirm")
    public ResponseEntity<VerifyCodeResponse> verifyCode(@RequestBody VerifyCodeRequest request){
        boolean isValid = emailVerificationService.verifyCode(
                request.getEmail(),
                request.getVerificationCode()
        );
        if (isValid) {
            String token = emailVerificationService.issueVerificationToken(request.getEmail());
            return ResponseEntity.ok(new VerifyCodeResponse("이메일 인증이 완료되었습니다.", token));
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new VerifyCodeResponse("인증 코드가 올바르지 않거나 만료되었습니다.", null));
        }
    }

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request){
        authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 완료");
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        AuthTokens tokens = authService.login(request);

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", tokens.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        LoginResponse body = LoginResponse.of(tokens.getAccessToken(), tokens.getAccessTokenExpiresIn());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(body);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@CookieValue("refreshToken") String refreshToken){
        String token = refreshToken.startsWith("Bearer ") ? refreshToken.substring(7) : refreshToken;

        //System.out.println("Extracted token = [" + token + "]");

        LoginResponse response = authService.renewAccessToken(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(){
        ResponseCookie deleteRefreshCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteRefreshCookie.toString())
                .body("로그아웃 되었습니다.");
    }


    //DTO 변환 책임 넘기기? 나중에
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
