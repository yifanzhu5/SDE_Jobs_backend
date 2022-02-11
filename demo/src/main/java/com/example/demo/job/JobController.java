package com.example.demo.job;

import com.example.demo.job.constant.GlobalConst;
import com.example.demo.job.entity.Job;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping(path = "api/v1")
public class JobController {
    private final JobService jobService;//automatically instantiated and injected into the constructor

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;//dependency injection
    }

    @GetMapping("/jobs/{pgid}")
    public JSONObject getAllJobs(@PathVariable int pgid) {//List<Job>
        return jobService.getAllJobs(pgid);
    }

    /**
     * Search by keywords + filter by locations/companies
     *
     * @param keywords  - in search bar
     * @param current_page - page index obtained from front end
     * @param page_size - how many in a page. 20 or 50 or 100
     * @param locations - locations
     * @param companies - companies
     * @return
     */
    @PostMapping(value = "/search")
    @ResponseBody
    public JSONObject search(@RequestParam(value = "keywords", required = false,defaultValue = "") String keywords,
                             @RequestParam(value = "current_page") int current_page,
                             @RequestParam(value = "page_size") int page_size,
                             @RequestParam(value = "locations", required = false,defaultValue = "") List<String> locations,
                             @RequestParam(value = "companies", required = false,defaultValue = "") List<String> companies) {

        return jobService.searchPosition(keywords,locations,companies,page_size,current_page);
    }

    @DeleteMapping(path = "{jobID}")
    public void deleteJob(@PathVariable("jobID") Long jobId) {
        jobService.deleteJob(jobId);
    }


}
