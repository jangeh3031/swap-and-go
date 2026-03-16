package com.swapandgo.sag.security.user;


import com.swapandgo.sag.domain.user.User;
import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {
    private final Long userId;
    private final String email;
    private final String password;

    public CustomUserDetails(User user){
        this.userId = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    //권한 표시 - ROLE + 권한명
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    //로그인 식별자
    @Override
    public String getUsername() {
        return email;
    }

    //계정 유효기간 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정 잠김 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정 활성 여부
    @Override
    public boolean isEnabled() {
        return true;
    }
}
