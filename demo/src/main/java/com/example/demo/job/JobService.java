package com.example.demo.job;


import com.example.demo.job.constant.GlobalConst;
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

    public JSONObject getAllJobs(int pgid) {
        List<Job> lAll = jobRepository.findAll();
        // TODO:  fetch db page by page
        return returnJobs(lAll, GlobalConst.PAGE_SIZE_MAX, pgid);
    }

    //return json after paging
    public JSONObject returnJobs(List<Job> jobs,
                                 Integer page_size,
                                 Integer current_page) {
        long count = jobs.size();
        JSONObject jsonObject = new JSONObject();
        if (count != 0) {
            int max_page_size = page_size;
            int pagesNum = (int) Math.ceil((double) count / page_size);
            page_size = current_page != pagesNum ? page_size : (int) (count % page_size);
            current_page = current_page < 1 || current_page > GlobalConst.MAX_PAGE || current_page > pagesNum ? 1 : current_page;
            jsonObject.put("count", count);
            jsonObject.put("current_page", current_page);
            jsonObject.put("page_size", page_size);
            //paging
            int begin = (current_page - 1) * max_page_size;
            int end = begin + page_size;
            List<Job> jobs_current_page = new ArrayList();
            for (int i = begin; i < end; i++) {
                jobs_current_page.add(jobs.get(i));
            }
            JSONArray json_jobs = JSONArray.fromObject(jobs_current_page);
            jsonObject.put("jobs", json_jobs);
        }
        else{//no search result
            jsonObject.put("count", 0);
            jsonObject.put("current_page", 1);
            jsonObject.put("page_size", 0);
            List<Job> jobs_current_page = new ArrayList();
            JSONArray json_jobs = JSONArray.fromObject(jobs_current_page);
            jsonObject.put("jobs", json_jobs);
        }
        return jsonObject;
    }

    public JSONObject searchPosition(String keywords,
                                     List<String> locations,//
                                     List<String> companies,
                                     Integer page_size,
                                     Integer current_page) {
        List<Job> jobsList = jobRepository.findAll();
        // TODO: fetch db page by page
        if (!locations.isEmpty()) {
            jobsList = jobRepository.findJobsByLocationsIn(locations);
        }
        if (!companies.isEmpty()) {
            List<Job> tmp2 = jobRepository.findJobsByCompanyIn(companies);
            jobsList.retainAll(tmp2);// get intersection
        }
        if(!keywords.isBlank()){
            List<Job> onSearch = jobRepository.findJobsByKeywords(keywords);
            jobsList.retainAll(onSearch);// get intersection
        }
        return returnJobs(jobsList, page_size, current_page);
    }


    public void deleteJob(Long jobId) {
        boolean exists = jobRepository.existsById(jobId);
        if (!exists) {
            throw new IllegalStateException("job with id " + jobId + " does not exist");
        }
        jobRepository.deleteById(jobId);
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
