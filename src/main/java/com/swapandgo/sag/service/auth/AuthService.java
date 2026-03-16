package com.swapandgo.sag.service.auth;

import com.swapandgo.sag.domain.user.User;
import com.swapandgo.sag.dto.auth.AuthTokens;
import com.swapandgo.sag.dto.auth.LoginRequest;
import com.swapandgo.sag.dto.auth.LoginResponse;
import com.swapandgo.sag.dto.auth.SignupRequest;
import com.swapandgo.sag.repository.UserRepository;
import com.swapandgo.sag.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailVerificationService emailVerificationService;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpirationTime;

    //회원가입
    public void signup(SignupRequest request){
        //토큰 겁증 및 이메일 추출
        String email = emailVerificationService.validateAndConsumeToken(request.getVerificationToken());
        if (email == null) {
            throw new IllegalArgumentException("유효하지 않거나 만효된 인증 토큰입니다.");
        }

        if(!email.equals(request.getEmail())){
            throw new IllegalArgumentException("인증된 이메일과 요청 이메일이 일치하지 않습니다.");
        }

        //이메일 중복 확인
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }


        //비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.createUser(
                request.getUsername(),
                request.getEmail(),
                encodedPassword,
                request.getAddress()
        );

        userRepository.save(user);

        emailVerificationService.removeVerificationCode(request.getEmail());
        log.info("새로운 사용자 등록되었습니다: {}", request.getEmail());
    }

    //로그인
    public AuthTokens login(LoginRequest request){
        try {
            //인증
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            //JWT 토큰 생성
            String accessToken = jwtTokenProvider.generateAccessToken(request.getEmail());
            String refreshToken = jwtTokenProvider.generateRefreshToken(request.getEmail());

            return AuthTokens.of(accessToken, refreshToken, accessTokenExpirationTime);

        }catch (AuthenticationException e){
            throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다.");
        }
    }

    //access 토큰 갱신 -> refreshToken은 로그인 할 때만 재발급 하는 로직
    public LoginResponse renewAccessToken(String refreshToken) {
        if(!jwtTokenProvider.validateToken(refreshToken)){
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        String email = jwtTokenProvider.getEmailFromToken(refreshToken);
        //DB저장은 일단 생략


        String newAccessToken = jwtTokenProvider.generateAccessToken(email);

        return LoginResponse.of(newAccessToken, accessTokenExpirationTime);

    }
}
