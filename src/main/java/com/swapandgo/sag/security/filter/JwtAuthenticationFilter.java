package com.swapandgo.sag.security.filter;

import com.swapandgo.sag.security.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    //의존 관계 주입임으로 빈으로 등록 해놓은 CustomUserDetailSevice가 주입 될 예정
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("[JwtAuthFilter] {} {} authHeaderPresent={} origin={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    request.getHeader("Authorization") != null,
                    request.getHeader("Origin"));
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                String email = jwtTokenProvider.getEmailFromToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                //첫 번째 값: UserDetails (로그인된 사용자 정보)
                // 두 번째 값: credentials → JWT 인증에서는 null(이미 인증됨)
                // 세 번째 값: 권한 목록(ROLE_USER 등)
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //인증된 사용자라고 스프링이 인식하게 됨 -> 컨트롤러에서 @AuthenticationPrincipal UserDetails user 자동 주입 등 가능
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("JWT 토큰 기반 인증 성공: {}", email);
            } else if (!StringUtils.hasText(jwt)) {
                log.debug("Authorization 헤더에 JWT 토큰이 없습니다.");
            } else {
                log.debug("JWT 토큰 검증 실패");
            }
        }catch (Exception e){
            log.error("JWT 인증 중 오류 발생: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        //Authorization: Bearer eyJhbGciOiJIUz... 부분 헤더에서 가져오기
        String bearerToken = request.getHeader("Authorization");
        if (!StringUtils.hasText(bearerToken)) {
            return null;
        }

        // "Bearer <token>" (대소문자 무시), 공백 여러 개 허용
        String[] parts = bearerToken.trim().split("\\s+");
        if (parts.length == 2 && parts[0].equalsIgnoreCase("Bearer")) {
            return parts[1].trim();
        }
        return null;

    }
}
