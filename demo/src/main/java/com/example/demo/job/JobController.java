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

@CrossOrigin(origins = "http://localhost:3000")
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

    @PostMapping(value = "/search")
    @ResponseBody
    public JSONObject search(@RequestBody HttpEntity httpEntity) {

        //parser
        Boolean has_remote = null;
        String keywords = "";
        List<String> city = new ArrayList<>();
        List<String> companies = new ArrayList<>();
        try {
            keywords = httpEntity.getKeywords().get();
        }catch (Exception e){}
        try {
            city = httpEntity.getCities().get();
        }catch (Exception e){}
        try {
            companies = httpEntity.getCompanys().get();
        }catch (Exception e){}
        Integer page_size =httpEntity.getPage_size().get();
        Integer current_page = httpEntity.getCurrent_page().get();
        try{
            has_remote = httpEntity.getHas_remote().get();
        }catch (Exception e){}


        //invoke search
        return jobService.searchPosition(keywords,companies,city,page_size,current_page,has_remote);
    }
        /*return jobService.searchPosition(
                httpEntity.getKeywords(),
                httpEntity.getLocations(),
                httpEntity.getCompanies(),
                httpEntity.getPage_size(),
                httpEntity.getCurrent_page());*/
}
