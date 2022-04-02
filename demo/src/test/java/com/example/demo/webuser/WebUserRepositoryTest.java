package com.example.demo.webuser;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.MySQLContainer;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WebUserRepositoryTest {

    @Autowired
    private WebUserRepository webUserRepository;

    @Test
    void findByEmailTest() {
        WebUser webUser = new WebUser("Kevin", "123456", "Kevin@gmail.com", WebUserRole.USER);
        webUserRepository.save(webUser);
        Optional<WebUser> res = webUserRepository.findByEmail(webUser.getEmail());
        assertEquals(webUser, res.get());
    }

    @Test
    void findByUsernameTest() {
        WebUser webUser = new WebUser("Kevin", "123456", "Kevin@gmail.com", WebUserRole.USER);
        webUserRepository.save(webUser);
        Optional<WebUser> res = webUserRepository.findByUsername(webUser.getUsername());
        assertEquals(webUser, res.get());
    }

    @Test
    void enableWebUserTest() {
        WebUser webUser = new WebUser("Kevin", "123456", "Kevin@gmail.com", WebUserRole.USER);
        webUserRepository.save(webUser);
        int res = webUserRepository.enableWebUser(webUser.getEmail());
        assertEquals(1, res);
    }
}