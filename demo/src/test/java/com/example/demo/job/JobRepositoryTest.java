package com.example.demo.job;

import com.example.demo.job.entity.Job;
import com.example.demo.job.constant.GlobalConst;
import net.sf.json.JSONObject;
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
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


//@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JobRepositoryTest {

//    @Container
    private static final MySQLContainer mySQLContainer= (MySQLContainer) new MySQLContainer("mysql:latest")
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
    @Sql("classpath:data.sql")
    void findJobsBy() {
        //All empty, should output all data from jobs_test
        Page<Job> expected=underTest.findJobsBy(new ArrayList<>(),new ArrayList<>(),null,"",pageable0);
        assertThat(expected.getTotalElements()).isEqualTo(24);
        assertThat(expected.getTotalPages()).isEqualTo(2);

        //Test has_remote true/null
        expected=underTest.findJobsBy(new ArrayList<>(),new ArrayList<>(),true,"",pageable0);
        assertThat(expected.getTotalElements()).isEqualTo(3);

        //Test keywords: count and match
        expected=underTest.findJobsBy(new ArrayList<>(),new ArrayList<>(),null,"develop",pageable0);
        assertThat(expected.getTotalElements()).isEqualTo(21);
        for(int i=0;i<20;i++){
            assertThat(expected.getContent().get(i)).asString().containsIgnoringCase("develop");
        }
        expected=underTest.findJobsBy(new ArrayList<>(),new ArrayList<>(),null,"develop",pageable1);
        assertThat(expected.getContent().get(0)).asString().containsIgnoringCase("develop");
    }

    @Test
    void findJobsBy_cityOthers() {

    }

    @Test
    void findJobsBy_companiesOthers() {
    }

    @Test
    void findJobsBy_companiesCityOthers() {
        //expected0: contains only other company and other city
        Page<Job> expected0 = underTest.findJobsBy_companiesCityOthers(
                new ArrayList<>(),
                new ArrayList<>(),
                null,
                "",
                GlobalConst.TOP5_CITIES,
                GlobalConst.TOP5_COMPANIES,
                pageable0
        );
        //expected1: contains Google and other company and other city
        Page<Job> expected1 = underTest.findJobsBy_companiesCityOthers(
                new ArrayList<>(Arrays.asList("Google")),
                new ArrayList<>(),
                null,
                "",
                GlobalConst.TOP5_CITIES,
                GlobalConst.TOP5_COMPANIES,
                pageable0
        );
        //expected2: contains other company and Toronto and other city
        Page<Job> expected2 = underTest.findJobsBy_companiesCityOthers(
                new ArrayList<>(),
                new ArrayList<>(Arrays.asList("Toronto")),
                null,
                "",
                GlobalConst.TOP5_CITIES,
                GlobalConst.TOP5_COMPANIES,
                pageable0
        );

        List<Page<Job>> expectedL = new ArrayList<>(Arrays.asList(expected0, expected1, expected2));
        for (int i = 0; i < expectedL.size(); i++) {
            assertThat(expectedL.get(i).getContent()).asList().hasSize(9);
            assertThat(expectedL.get(i).getContent()).asList()
                    .extracting("company").doesNotContain("Amazon", "Google", "Shopify", "Microsoft");
            assertThat(expectedL.get(i).getContent()).asList()
                    .extracting("city").doesNotContain("Vancouver", "Toronto", "Waterloo", "Montreal", "Ottawa");
        }
    }
}