package com.swapandgo.sag.service.user;

import com.swapandgo.sag.domain.user.User;
import com.swapandgo.sag.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("유저를 찾을 수 없습니다 : " +email));
    }
}
