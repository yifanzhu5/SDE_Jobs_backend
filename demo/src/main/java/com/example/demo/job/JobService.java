package com.example.demo.job;


import com.example.demo.job.constant.GlobalConst;
import com.example.demo.job.entity.Job;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class JobService {//service class

    private final JobRepository jobRepository;

    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public JSONObject getAllJobs(int pgid) {
        Page<Job> jobs = jobRepository.findAll(
                PageRequest.of(
                        //page starts from 0 in pageable
                        pgid - 1,
                        GlobalConst.PAGE_SIZE_MAX_20
                ));
        return returnJobs(jobs);
    }

    //return json after paging
    public JSONObject returnJobs(Page<Job> jobs) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", jobs.getTotalElements());
        jsonObject.put("current_page", jobs.getNumber() + 1);
        jsonObject.put("page_size", jobs.getNumberOfElements());
        JSONArray json_jobs = JSONArray.fromObject(jobs.getContent());
        jsonObject.put("jobs", json_jobs);
        return jsonObject;
    }

    public JSONObject searchPosition(String keywords,
                                     List<String> companies,//
                                     List<String> city,
                                     Integer page_size,
                                     Integer current_page,
                                     Boolean has_remote) {
        Pageable pageable=PageRequest.of(
                //page starts from 0 in pageable
                current_page - 1,
                page_size
        );
        if(keywords.isBlank()){
            keywords="";
        }
        Page<Job> jobs;
        Boolean flag_companies=companies.remove("others");
        Boolean flag_city=city.remove("others");
        //both contain "others"
        if(flag_companies&&flag_city){
            jobs=jobRepository.findJobsBy_companiesCityOthers(
                    companies,
                    city,
                    has_remote,
                    keywords,
                    GlobalConst.TOP5_CITIES,
                    GlobalConst.TOP5_COMPANIES,
                    pageable
            );
        }
        //companies doesn't contain "others"; city does.
        else if(flag_city){
            jobs=jobRepository.findJobsBy_cityOthers(
                    companies,
                    city,
                    has_remote,
                    keywords,
                    GlobalConst.TOP5_CITIES,
                    pageable
            );
        }
        //city doesn't contain "others"; companies does.
        else if(flag_companies){
            jobs=jobRepository.findJobsBy_companiesOthers(
                    companies,
                    city,
                    has_remote,
                    keywords,
                    GlobalConst.TOP5_COMPANIES,
                    pageable
            );
        }
        else{
            jobs = jobRepository.findJobsBy(
                    companies,
                    city,
                    has_remote,
                    keywords,
                    pageable
            );
        }
        return returnJobs(jobs);
    }

//    @Transactional
//    public void updateJob(Long jobId, String name, String href) {
//        Job job = jobRepository.findById(jobId).orElseThrow(() -> new IllegalStateException(
//                "job with id "+jobId+" does not exist"));
//        if(name!=null && name.length()>0 && !Objects.equals(job.getTitle(),name)){
//            job.setTitle(name);
//        }
//        if(href!=null && href.length()>0 &&!Objects.equals(job.getFrom_url(),href)){
//            Optional<Job> jobOptional =jobRepository.findJobByFrom_url(href);
//            if(jobOptional.isPresent()){
//                throw new IllegalStateException("href taken");
//            }
//            job.setFrom_url(href);
//        }
//    }
}
