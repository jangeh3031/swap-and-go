package com.swapandgo.sag.repository;

import com.swapandgo.sag.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//@Repository 생략가능
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

}
