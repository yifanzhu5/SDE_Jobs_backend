package com.example.demo.job;

import com.example.demo.job.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    @Query(value = "select s from Job s where " +
            "(COALESCE(:companies, 'empty') = 'empty' or s.company in :companies) and " +
            "(COALESCE(:city, 'empty') = 'empty' or s.city in :city) and " +
            "(:has_remote is null or s.has_remote = :has_remote) and " +
            "(:keywords = '' or s.title like concat('%',:keywords ,'%') " +
            "or s.description like concat('%',:keywords ,'%') " +
            "or s.basic_qualifications like concat('%',:keywords ,'%') " +
            "or s.locations like concat('%',:keywords ,'%') " +
            "or s.job_category like concat('%',:keywords ,'%') " +
            "or s.team like concat('%',:keywords ,'%') " +
            "or s.company like concat('%',:keywords ,'%') " +
            "or s.preferred_qualifications like concat('%',:keywords ,'%'))")
    Page<Job> findJobsByCompanyInAndLocationsInAndHas_remoteAndKeywords(
            @Param("companies") List<String> companiesL,
            @Param("city") List<String> cityL,
            @Param("has_remote") Boolean has_remote,
            @Param("keywords") String keywords,
            Pageable pageable);

    @Query(value = "select s from Job s where " +
            "(COALESCE(:companies, 'empty') = 'empty' or s.company in :companies) and " +
            "(s.city not in :top5_cities or s.city in :city) and " +
            "(:has_remote is null or s.has_remote = :has_remote) and " +
            "(:keywords = '' or s.title like concat('%',:keywords ,'%') " +
            "or s.description like concat('%',:keywords ,'%') " +
            "or s.basic_qualifications like concat('%',:keywords ,'%') " +
            "or s.locations like concat('%',:keywords ,'%') " +
            "or s.job_category like concat('%',:keywords ,'%') " +
            "or s.team like concat('%',:keywords ,'%') " +
            "or s.company like concat('%',:keywords ,'%') " +
            "or s.preferred_qualifications like concat('%',:keywords ,'%'))")
    Page<Job> findJobsByCompanyInAndLocationsInAndHas_remoteAndKeywords_cityOthers(
            @Param("companies") List<String> companiesL,
            @Param("city") List<String> cityL,
            @Param("has_remote") Boolean has_remote,
            @Param("keywords") String keywords,
            @Param("top5_cities") List<String> top5_cities,
            Pageable pageable);

    @Query(value = "select s from Job s where " +
            "(s.company not in (:top5_companies) or s.company in :companies) and " +
            "(COALESCE(:city, 'empty') = 'empty' or s.city in :city) and " +
            "(:has_remote is null or s.has_remote = :has_remote) and " +
            "(:keywords = '' or s.title like concat('%',:keywords ,'%') " +
            "or s.description like concat('%',:keywords ,'%') " +
            "or s.basic_qualifications like concat('%',:keywords ,'%') " +
            "or s.locations like concat('%',:keywords ,'%') " +
            "or s.job_category like concat('%',:keywords ,'%') " +
            "or s.team like concat('%',:keywords ,'%') " +
            "or s.company like concat('%',:keywords ,'%') " +
            "or s.preferred_qualifications like concat('%',:keywords ,'%'))")
    Page<Job> findJobsByCompanyInAndLocationsInAndHas_remoteAndKeywords_companiesOthers(
            @Param("companies") List<String> companiesL,
            @Param("city") List<String> cityL,
            @Param("has_remote") Boolean has_remote,
            @Param("keywords") String keywords,
            @Param("top5_companies") List<String> top5_companies,
            Pageable pageable);

    @Query(value = "select s from Job s where " +
            "(s.company not in :top5_companies or s.company in :companies) and " +
            "(s.city not in :top5_cities or s.city in :city) and " +
            "(:has_remote is null or s.has_remote = :has_remote) and " +
            "(:keywords = '' or s.title like concat('%',:keywords ,'%') " +
            "or s.description like concat('%',:keywords ,'%') " +
            "or s.basic_qualifications like concat('%',:keywords ,'%') " +
            "or s.locations like concat('%',:keywords ,'%') " +
            "or s.job_category like concat('%',:keywords ,'%') " +
            "or s.team like concat('%',:keywords ,'%') " +
            "or s.company like concat('%',:keywords ,'%') " +
            "or s.preferred_qualifications like concat('%',:keywords ,'%'))")
    Page<Job> findJobsByCompanyInAndLocationsInAndHas_remoteAndKeywords_Others(
            @Param("companies") List<String> companiesL,
            @Param("city") List<String> cityL,
            @Param("has_remote") Boolean has_remote,
            @Param("keywords") String keywords,
            @Param("top5_cities") List<String> top5_cities,
            @Param("top5_companies") List<String> top5_companies,
            Pageable pageable);
}
