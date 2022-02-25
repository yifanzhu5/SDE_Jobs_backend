package com.example.demo.job;

import com.example.demo.job.constant.GlobalConst;
import com.example.demo.job.entity.Job;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.domain.Pageable;
import static org.junit.jupiter.api.Assertions.*;

//unit test
@SpringBootTest
class JobServiceTest {

    private JobService jobService;

    @Mock
    private JobRepository jobRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        //jobRepository = Mockito.mock(JobRepository.class);
        jobService = new JobService(jobRepository);
    }

    @Test
    void getAllJobs() {
    }

    @Test
    void returnJobs() {
    }

    String keywords = "software";
    Boolean has_remote = true;
    int page_size = 20;
    int current_page = 1;

    @Test
        //case 1 : both contain "others"
    void searchPosition_companiesCityOthers() {
        List<String> companies = new ArrayList<>(Arrays.asList("Google", "others"));
        List<String> cities = new ArrayList<>(Arrays.asList("Toronto", "others"));

        Pageable pageable = PageRequest.of (
                current_page-1,
                page_size
        );

        Job job = new Job();
        job.setId(1L);
        Page<Job> jobs = new PageImpl<>(List.of(job));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", jobs.getTotalElements());
        jsonObject.put("current_page", jobs.getNumber() + 1);
        jsonObject.put("page_size", jobs.getNumberOfElements());
        JSONArray json_jobs = JSONArray.fromObject(jobs.getContent());
        jsonObject.put("jobs", json_jobs);

        Mockito.when(jobRepository.findJobsBy_companiesCityOthers(
                companies,
                cities,
                has_remote,
                keywords,
                GlobalConst.TOP5_CITIES,
                GlobalConst.TOP5_COMPANIES,
                pageable)).thenReturn(jobs);

        //test
        JSONObject res = jobService.searchPosition(
                keywords,
                companies,
                cities,
                page_size,
                current_page,
                has_remote
        );

        //verify if method returns right object
        assertEquals(jsonObject, res);

        //verify if findJobsBy_companiesCityOthers has been called
        Mockito.verify(jobRepository).findJobsBy_companiesCityOthers(
                companies,
                cities,
                has_remote,
                keywords,
                GlobalConst.TOP5_CITIES,
                GlobalConst.TOP5_COMPANIES,
                pageable
        );

        System.out.println(res.getJSONArray("jobs").getJSONObject(0).getString("id"));

    }

    @Test
        //case 2 : companies doesn't contain "others"; city does.
    void searchPosition_cityOthers() {
        List<String> companies = new ArrayList<>(List.of("Google"));
        List<String> cities = new ArrayList<>(Arrays.asList("Toronto", "others"));

        Pageable pageable = PageRequest.of(
                current_page-1,
                page_size
        );

        Job job = new Job();
        job.setId(2L);
        Page<Job> jobs = new PageImpl<>(List.of(job));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", jobs.getTotalElements());
        jsonObject.put("current_page", jobs.getNumber() + 1);
        jsonObject.put("page_size", jobs.getNumberOfElements());
        JSONArray json_jobs = JSONArray.fromObject(jobs.getContent());
        jsonObject.put("jobs", json_jobs);

        Mockito.when(jobRepository.findJobsBy_cityOthers(
                companies,
                cities,
                has_remote,
                keywords,
                GlobalConst.TOP5_CITIES,
                pageable)).thenReturn(jobs);

        //test
        JSONObject res = jobService.searchPosition(
                keywords,
                companies,
                cities,
                page_size,
                current_page,
                has_remote
        );

        //verify if method returns right object
        assertEquals(jsonObject, res);

        //verify if findJobsBy_cityOthers has been called
        Mockito.verify(jobRepository).findJobsBy_cityOthers(
                companies,
                cities,
                has_remote,
                keywords,
                GlobalConst.TOP5_CITIES,
                pageable
        );

        System.out.println(res.getJSONArray("jobs").getJSONObject(0).getString("id"));

    }

    @Test
        //case 3 : city doesn't contain "others"; companies does.
    void searchPosition_companiesOthers() {
        List<String> companies = new ArrayList<>(Arrays.asList("Google", "others"));
        List<String> cities = new ArrayList<>(List.of("Toronto"));

        Pageable pageable = PageRequest.of(
                current_page-1,
                page_size
        );

        Job job = new Job();
        job.setId(3L);
        Page<Job> jobs = new PageImpl<>(List.of(job));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", jobs.getTotalElements());
        jsonObject.put("current_page", jobs.getNumber() + 1);
        jsonObject.put("page_size", jobs.getNumberOfElements());
        JSONArray json_jobs = JSONArray.fromObject(jobs.getContent());
        jsonObject.put("jobs", json_jobs);

        Mockito.when(jobRepository.findJobsBy_companiesOthers(
                companies,
                cities,
                has_remote,
                keywords,
                GlobalConst.TOP5_COMPANIES,
                pageable)).thenReturn(jobs);

        //test
        JSONObject res = jobService.searchPosition(
                keywords,
                companies,
                cities,
                page_size,
                current_page,
                has_remote
        );

        //verify if method returns right object
        assertEquals(jsonObject, res);

        //verify if findJobsBy_companiesOthers has been called
        Mockito.verify(jobRepository).findJobsBy_companiesOthers(
                companies,
                cities,
                has_remote,
                keywords,
                GlobalConst.TOP5_COMPANIES,
                pageable
        );

        System.out.println(res.getJSONArray("jobs").getJSONObject(0).getString("id"));
    }

    @Test
        //case 4 : no others.
    void searchPosition_noOthers() {
        List<String> companies = new ArrayList<>(List.of("Google"));
        List<String> cities = new ArrayList<>(List.of("Toronto"));

        Pageable pageable = PageRequest.of(
                current_page-1,
                page_size
        );

        Job job = new Job();
        job.setId(4L);
        Page<Job> jobs = new PageImpl<>(List.of(job));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", jobs.getTotalElements());
        jsonObject.put("current_page", jobs.getNumber() + 1);
        jsonObject.put("page_size", jobs.getNumberOfElements());
        JSONArray json_jobs = JSONArray.fromObject(jobs.getContent());
        jsonObject.put("jobs", json_jobs);

        Mockito.when(jobRepository.findJobsBy(
                companies,
                cities,
                has_remote,
                keywords,
                pageable)).thenReturn(jobs);

        //test
        JSONObject res = jobService.searchPosition(
                keywords,
                companies,
                cities,
                page_size,
                current_page,
                has_remote
        );

        //verify if method returns the right object
        assertEquals(jsonObject, res);

        //verify if findJobsBy has been called
        Mockito.verify(jobRepository).findJobsBy(
                companies,
                cities,
                has_remote,
                keywords,
                pageable
        );

        System.out.println(res.getJSONArray("jobs").getJSONObject(0).getString("id"));

    }
}