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

    @GetMapping("/jobs")
    public JSONObject getAllJobs() {//List<Job>
        return jobService.getAllJobs();
    }


    public void returnJobs(@RequestBody Job job) {
        jobService.returnJob(job);
    }

    /**
     * Search by keywords + filter by locations/companies
     *
     * @param keywords  - in search bar
     * @param current_page      - page index from front end
     * @param page_size - how many in a page. 20 or 50 or 100
     * @param locations - locations
     * @param companies - companies
     * @return
     */
    @PostMapping(value = "/search")
    @ResponseBody
    public String search(@RequestParam(value = "keywords", defaultValue = "") String keywords,
                         @RequestParam(value = "current_page") int current_page,
                         @RequestParam(value = "page_size") int page_size,
                         @RequestParam(value = "locations", defaultValue = "") List<String> locations,
                         @RequestParam(value = "companies", defaultValue = "") List<String> companies) {

        current_page = current_page < 1 || current_page > GlobalConst.MAX_PAGE ? 1 : current_page;
        List<Job> posInfo = jobService.searchPosition(keywords,locations,companies);

        Map output = new TreeMap();
        output.put("user", user);
        output.put("title", ("第" + page + "页"));
        output.put("keyword", keyword);
        output.put("orderBy", orderBy);
        output.put("posInfo", posInfo);

        JSONObject jsonObject = JSONObject.fromObject(output);

        return jsonObject.toString();
    }

    @DeleteMapping(path = "{jobID}")
    public void deleteJob(@PathVariable("jobID") Long jobId) {
        jobService.deleteJob(jobId);
    }


}
