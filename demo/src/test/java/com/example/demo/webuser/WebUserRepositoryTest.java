package com.example.demo.webuser;

import com.example.demo.job.JobRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MySQLContainer;

import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;


import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("classpath:user.sql")
@RunWith(SpringRunner.class)
class WebUserRepositoryTest {

    //    @Container
    private static final MySQLContainer mySQLContainer = (MySQLContainer) new MySQLContainer("mysql:latest")
            .withReuse(true);

    @BeforeAll
    public static void setup() {
        mySQLContainer.start();
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @Autowired
    private WebUserRepository underTest;

    @Test
    void enableWebUserSucceed() {
        //has record
        int expected = underTest.enableWebUser("zhang@gmail.com");
        assertEquals(expected, 1);
    }

    @Test
    void enableWebUserFailed() {
        //has record
        int expected = underTest.enableWebUser("zhan@gmail.com");
        assertEquals(expected, 0);
    }
}