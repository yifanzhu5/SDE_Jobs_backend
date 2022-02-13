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

    @Query(value = "select s from Job s where s.company in :companies")
    List<Job> findJobsByCompanyIn(@Param("companies") List<String> companiesL);

    //TODO: others
    @Query(value = "select s from Job s where s.locations in :locs")
    List<Job> findJobsByLocationsIn(@Param("locs") List<String> locationsL);

    @Query(value = "select s from Job s where s.locations =?1")
    List<Job> findJobsByLocations(String locations);

    //CONCAT_WS('', column1, column2, column3) LIKE '%keyword%' may affect performance
    //    @Query(value="select s from Job s where CONCAT(s.title,' ',s.description,' ',s.basic_qualifications,' ',s.locations,' ',s.company,' ',s.job_category,' ',s.team,' ',s.preferred_qualifications) like %?1%")
    @Query(value = "select s from Job s where s.title like %?1%" +
            " or s.description like %?1%" +
            " or s.basic_qualifications like %?1%" +
            " or s.locations like %?1%" +
            " or s.job_category like %?1%" +
            " or s.team like %?1%" +
            " or s.company like %?1%" +
            " or s.preferred_qualifications like %?1%")
    List<Job> findJobsByKeywords(String keywords);
}
