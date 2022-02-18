package com.example.demo.job;

import com.example.demo.job.constant.GlobalConst;
import com.example.demo.job.entity.Job;
import com.example.demo.job.entity.HttpEntity;
import net.sf.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;


@RestController
@RequestMapping(path = "api/v1/jobs")
public class JobController {
    private final JobService jobService;//automatically instantiated and injected into the constructor

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;//dependency injection
    }

    @GetMapping("page/{pgid}")
    public JSONObject getAllJobs(@PathVariable int pgid) {//List<Job>
        return jobService.getAllJobs(pgid);
    }

    /*  Deprecate as cannot parse Content-Type: application/json
        @PostMapping(value = "/search")
        @ResponseBody
        public JSONObject search(@RequestParam(value = "keywords", required = false, defaultValue = "Software") String keywords,
                                 @RequestParam(value = "current_page", required = false, defaultValue = "1") Integer current_page,
                                 @RequestParam(value = "page_size", required = false, defaultValue = "20") Integer page_size,
                                 @RequestParam(value = "locations", required = false, defaultValue = "") String[] locationsArray,
                                 @RequestParam(value = "companies", required = false, defaultValue = "") String[] companiesArray) {
    //        locations=new ArrayList<String>(); locations.add("CA, BC, Vancouver");
    //        companies=new ArrayList<String>(); companies.add("Amazon Dev Centre Canada ULC");
            List<String> locations= Arrays.asList(locationsArray);
            List<String> companies= Arrays.asList(companiesArray);
            return jobService.searchPosition(keywords, locations, companies, page_size, current_page);
        }*/
    @PostMapping(value = "/search")
    @ResponseBody
    public JSONObject search(@RequestBody HttpEntity httpEntity) {

        //parser
        Boolean has_remote = null;
        Integer update_time = null;
        String keywords = null;
        List<String> locations = new ArrayList<>();
        List<String> companies = new ArrayList<>();
        if (httpEntity.getKeywords().isPresent()){
            keywords = httpEntity.getKeywords().get();
        }
        try {
            locations = httpEntity.getLocations().get();
        }catch (Exception e){}
        try {
            companies = httpEntity.getCompanies().get();
        }catch (Exception e){}
        Integer page_size =httpEntity.getPage_size().get();
        Integer current_page = httpEntity.getCurrent_page().get();
        try{
            update_time = httpEntity.getUpdate_time().get();
        }catch (Exception e){}
        try{
            has_remote = httpEntity.getHas_remote().get();
        }catch (Exception e){}
        //invoke search
        return jobService.searchPosition(keywords,locations,companies,page_size,current_page,update_time,has_remote);
    }
        /*return jobService.searchPosition(
                httpEntity.getKeywords(),
                httpEntity.getLocations(),
                httpEntity.getCompanies(),
                httpEntity.getPage_size(),
                httpEntity.getCurrent_page());*/


    @DeleteMapping(path = "{jobID}")
    public void deleteJob(@PathVariable("jobID") Long jobId) {
        jobService.deleteJob(jobId);
    }
}
