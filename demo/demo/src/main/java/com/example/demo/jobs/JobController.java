package com.example.demo.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/job")
public class JobController {
    private final JobService jobService;

    @Autowired
    public JobController(JobService JobService) {
        this.jobService = JobService;
    }
    @GetMapping
    public List<Job> getJobs() {
        //TODO for jobs
        return jobService.getJobs();
    }


    @PostMapping
    public void registerNewJob(@RequestBody Job job) {
        jobService.addNewJob(job);
    }

    @PutMapping(path = "{jobId}")
    public void updateJob(
            @PathVariable("jobId") Long jobId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email
    ) {
        jobService.updateJob(jobId, name, email);
    }

    @DeleteMapping(path = "{jobId}")
    public void deleteJob(@PathVariable("jobId") Long jobId) {
        jobService.deleteJob(jobId);
    }
}
