package com.example.demo.job;

import com.example.demo.job.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

//    @Query(value="select s from Job s where s.company=?1 and s.locations=?2",nativeQuery = true)
//    List<Job> findJobsByCompanyAndLocations(String company,String locations);

    @Query(value="select s from Job s where s.company in :companies")
    List<Job> findJobsByCompanyIn(@Param("companies") List<String> companiesL);

    @Query(value="select s from Job s where s.locations in :locs")
    List<Job> findJobsByLocationsIn(@Param("locs") List<String> locationsL);
}
