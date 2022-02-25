package com.example.demo.job;

import com.example.demo.job.entity.Job;
import com.example.demo.job.constant.GlobalConst;
import org.junit.Before;
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

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


//unit test
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@TestPropertySource(properties = {
//        "spring.datasource.url=jdbc:mysql://localhost:3306/job?useSSL=false"
//})
class JobRepositoryTest {

    @Autowired
    private JobRepository underTest;

    private final Pageable pageable=PageRequest.of(
            //page starts from 0 in pageable
            0,
            GlobalConst.PAGE_SIZE_MAX
    );

    @Test
    @Sql("/data.sql")
    void findJobsBy() {
//        List<Job> l=underTest.findAll();
//        assertThat(l.size()).isEqualTo(23);
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