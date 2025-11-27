package com.swapandgo.sag.repository;

import com.swapandgo.sag.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
