package com.swapandgo.sag.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final RedisTemplate<String, String> redisTemplate;
    private final EmailSender emailSender;

    private static final int CODE_LENGTH = 6;
    private static final int CODE_EXPIRATION_MINUTES = 5;
    private static final int TOKEN_EXPIRATION_MINUTES = 10;
    private static final String CODE_KEY_PREFIX = "email:verification:code:";
    private static final String TOKEN_KEY_PREFIX = "email:verification:token:";

    //인증 코드 생성 및 발송
    public void sendVerificationCode(String email){
        String code = generateCode();
        String key = CODE_KEY_PREFIX + email;

        //Redis에 저장
        redisTemplate.opsForValue().set(
                key,
                code,
                CODE_EXPIRATION_MINUTES,
                TimeUnit.MINUTES
        );

        emailSender.sendVerificationEmail(email, code);

        log.info("인증 코드 발송 완료 : {}", email);
    }

    //인증 코드 검증
    public boolean verifyCode(String email, String code){
        String key = CODE_KEY_PREFIX + email;
        String storedCode = redisTemplate.opsForValue().get(key);

        if (storedCode == null) {
            log.warn("인증 코드가 존재하지 않거나 만료되었습니다: {}", email);
            return false;
        }

        if (!storedCode.equals(code)) {
            log.warn("인증 코드가 일치하지 않습니다: {}", email);
            return false;
        }

        log.info("인증 코드 검증 성공: {}", email);
        return true;
    }

    //인증 성공 시 토큰 발급
    public String issueVerificationToken(String email){
        String token = UUID.randomUUID().toString();
        String key = TOKEN_KEY_PREFIX + token;

        //토큰에 이메일 저장
        redisTemplate.opsForValue().set(
                key,
                email,
                TOKEN_EXPIRATION_MINUTES,
                TimeUnit.MINUTES
        );

        log.info("인증 토큰 발급 : {} -> {}", email, token);
        return token;
    }

    //토큰으로 이메일 확인 및 토큰 삭제
    public String validateAndConsumeToken(String token){
        String key = TOKEN_KEY_PREFIX + token;
        String email = redisTemplate.opsForValue().get(key);

        if (email == null) {
            log.warn("유효하지 않거나 만료된 토큰: {}", token);
            return null;
        }

        //토큰삭제
        redisTemplate.delete(key);
        log.info("토큰 사용 완료: {} -> {}", token, email);

        return email;
    }

    //인증 코드 삭제
    public void removeVerificationCode(String email){
        String key = CODE_KEY_PREFIX + email;
        redisTemplate.delete(key);
        log.info("인증 코드 삭제: {}", email);
    }

    //코드 생성
    private String generateCode(){
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}
