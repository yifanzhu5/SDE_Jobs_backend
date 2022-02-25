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


@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/jobtest?useSSL=false"
})
class JobRepositoryTest {

//    @BeforeAll
//    static void init() {
//        new MySQLContainer("mysql")
//                .withDatabaseName("jobtest")
//                .withUsername("root")
//                .withPassword("65111")
//                .start();
//    }

    @Container
    MySQLContainer mySQLContainer=new MySQLContainer("mysql:latest")
        .withDatabaseName("jobtest")
        .withUsername("root")
        .withPassword("65111");

    @Autowired
    private JobRepository underTest;

    private final Pageable pageable=PageRequest.of(
            //page starts from 0 in pageable
            0,
            GlobalConst.PAGE_SIZE_MAX_20
    );

    @Test
    @Sql("/data.sql")
    void findJobsBy() {
        Page<Job> expected=underTest.findJobsBy(new ArrayList<>(),new ArrayList<>(),null,"",pageable);
        assertThat(expected.getContent()).asList().hasSize(20);
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