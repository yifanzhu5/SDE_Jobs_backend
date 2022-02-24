package com.example.demo.job;

import com.example.demo.job.entity.Job;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


//unit test
class JobRepositoryTest {

    @Autowired
    private JobRepository underTest;

    @Test
    void findJobsByCompanyInAndLocationsInAndHas_remoteAndKeywords() {
    }

    @Test
    void findJobsByCompanyInAndLocationsInAndHas_remoteAndKeywords_cityOthers() {
    }

    @Test
    void findJobsByCompanyInAndLocationsInAndHas_remoteAndKeywords_companiesOthers() {
    }

    @Test
    void findJobsByCompanyInAndLocationsInAndHas_remoteAndKeywords_Others() {
    }
}