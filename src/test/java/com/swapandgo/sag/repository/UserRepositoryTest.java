package com.swapandgo.sag.repository;

import com.swapandgo.sag.domain.user.Address;
import com.swapandgo.sag.domain.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @Rollback(value = false)
    public void testUser(){
        User user = User.createUser("user1", "jang@naver.com", "abc1234",
                new Address("Korea", "Daegu", "bukguro-1"));
        User savedUser = userRepository.save(user);

        User findUser = userRepository.findById(savedUser.getId()).get();

        Assertions.assertThat(findUser.getId()).isEqualTo(user.getId());
    }

}