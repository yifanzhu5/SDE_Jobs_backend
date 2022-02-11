package com.example.demo.job;


import com.example.demo.job.entity.Job;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class JobService {//service class

    private final JobRepository jobRepository;

    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public JSONObject getAllJobs() {
        List<Job> lAll = jobRepository.findAll();

        long count = lAll.size();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("count", count);
        jsonObject.put("current_page", "1");
        jsonObject.put("page_size", "1");

        //jobs是个数组,其实就是嵌套json
        JSONArray json_jobs = JSONArray.fromObject(lAll);
        jsonObject.put("jobs", json_jobs);
        return jsonObject;
    }


    public void returnJob(Job job) {
        List<Job> jobOptional = jobRepository.findJobsByCompany(job.getCompany());
        if (jobOptional.isPresent()) {
            throw new IllegalStateException("href taken");
        }
        jobRepository.save(job);//save into database
    }

    public void deleteJob(Long jobId) {
        boolean exists = jobRepository.existsById(jobId);
        if (!exists) {
            throw new IllegalStateException("job with id " + jobId + " does not exist");
        }
        jobRepository.deleteById(jobId);
    }

    public List<Job> searchPosition(String keywords, List<String> locations, List<String> companies) {
        List<Job> locationsRes=new ArrayList<>();
        for(String location :locations) {
            List<Job> temp = jobRepository.findJobsByLocations(location);
            locationsRes.addAll(temp);
        }
        List<Job> companyRes=new ArrayList<>();
        for(String company :companies) {
            List<Job> temp = jobRepository.findJobsByCompany(company);
            companyRes.addAll(temp);
        }
        locationsRes.retainAll(companyRes);// get intersection
        return locationsRes;
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
