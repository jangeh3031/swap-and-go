package com.swapandgo.sag;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.swapandgo.sag.domain.user.Address;
import com.swapandgo.sag.domain.user.QUser;
import com.swapandgo.sag.domain.user.User;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.swapandgo.sag.domain.user.QUser.user;

@SpringBootTest
@Tag("integration")
@Transactional
public class QuerydslBasicTest {
    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
        User user = User.createUser("eunho", "jan3031@naver.com", "password", new Address("germany", "ulm", "8"));
        em.persist(user);

        em.flush();
        em.clear();
    }

    @Test
    public void querydslTest() {
        User user1 = queryFactory
                .select(QUser.user)
                .from(QUser.user)
                .where(QUser.user.email.eq("jan3031@naver.com"))
                .fetchOne();
        Assertions.assertThat(user1.getUsername()).isEqualTo("eunho");
    }
}
