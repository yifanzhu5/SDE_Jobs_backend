package com.example.demo.job;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/job")
public class JobController {
    private final JobService jobService;//automatically instantiated and injected into the constructor

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;//dependency injection
    }

    @GetMapping
    public JSONObject getJobs() {//List<Job>
        return jobService.getJobs();
    }

//    @PostMapping
//    public void registerNewJob(@RequestBody Job job) {
//        jobService.addNewJob(job);
//    }

    @DeleteMapping(path = "{jobID}")
    public void deleteJob(@PathVariable("jobID") Long jobId) {
        jobService.deleteJob(jobId);
    }


}
