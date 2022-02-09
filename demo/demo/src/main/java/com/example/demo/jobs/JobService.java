package com.example.demo.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class JobService {
    private final JobRepository jobRepository;

    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<Job> getJobs() {
        //return jobRepository.findall();
        return jobRepository.//filter//page


    }
     // JPA filter 实现
    private List<Job> filtered(List<Job>){
        //
        return
    }
    public void addNewJob(Job job) {
        Optional<Job> jobOptional = jobRepository.
                findJobByEmail(job.getEmail());
        if (jobOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        jobRepository.save(job);
    }

    public void deleteJob(Long jobId) {
        boolean exists = jobRepository.existsById(jobId);
        if (!exists) {
            throw new IllegalStateException(
                    "job with id" + jobId + "does not exists"
            );
        }
        jobRepository.deleteById(jobId);
    }

    @Transactional
    public void updateJob(
            Long jobId,
            String name,
            String email) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalStateException(
                        "job with id " + jobId + " does not exist"
                ));

        if (name != null &&
            name.length() > 0 &&
            !Objects.equals(job.getName(), name)) {
            job.setName(name);
        }

        if (email != null &&
                email.length() > 0 &&
                !Objects.equals(job.getEmail(), email)) {
            Optional<Job> jobOptional = jobRepository
                    .findJobByEmail(email);
            if (jobOptional.isPresent()) {
                throw new IllegalStateException("email taken");
            }
            job.setEmail(email);
        }
    }
}
