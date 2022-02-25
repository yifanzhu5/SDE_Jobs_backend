package com.example.demo.job;

import com.example.demo.job.entity.Job;
import com.example.demo.job.constant.GlobalConst;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


//@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JobRepositoryTest {


//    @Container
    private static MySQLContainer mySQLContainer= (MySQLContainer) new MySQLContainer("mysql:latest")
        .withReuse(true);

    @BeforeAll
    public static void setup() {
        mySQLContainer.start();
    }
    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url",mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username",mySQLContainer::getUsername);
        registry.add("spring.datasource.password",mySQLContainer::getPassword);
    }

    @Autowired
    private JobRepository underTest;

    private final Pageable pageable0=PageRequest.of(
            //page starts from 0 in pageable
            0,
            GlobalConst.PAGE_SIZE_MAX_20
    );

    private final Pageable pageable1=PageRequest.of(
            //page starts from 0 in pageable
            1,
            GlobalConst.PAGE_SIZE_MAX_20
    );

    @Test
    @Sql("/data.sql")
    void findJobsBy() {
        Page<Job> expected0=underTest.findJobsBy(new ArrayList<>(),new ArrayList<>(),null,"",pageable0);
        assertThat(expected0.getContent()).asList().hasSize(20);
        Page<Job> expected1=underTest.findJobsBy(new ArrayList<>(),new ArrayList<>(),null,"",pageable1);
        assertThat(expected1.getContent()).asList().hasSize(3);
    }

    @Test
    void findJobsBy_cityOthers() {
    }

    @Test
    void findJobsBy_companiesOthers() {
    }

    @Test
    void findJobsBy_companiesCityOthers() {
    }
}