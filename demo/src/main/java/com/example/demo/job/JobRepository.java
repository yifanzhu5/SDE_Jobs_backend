package com.example.demo.job;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job,Long> {

//    @Query("select s from Job s where s.from_url=?1")
//    Optional<Job> findJobByFrom_url(String href);
}
