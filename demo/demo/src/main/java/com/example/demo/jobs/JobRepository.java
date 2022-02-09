package com.example.demo.job;

～import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRepository
        extends JpaRepository<Job,Long> {
    @Query("SELECT s FROM Student s WHERE s.email = ?1")
    Optional<Job> findJobByEmail(String email);
}
