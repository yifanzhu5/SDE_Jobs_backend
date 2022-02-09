package com.example.demo.jobs;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Table
public class Job {
    @Id
    @SequenceGenerator(
            name = "job_sequence",
            sequenceName = "job_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "job_sequence"
    )

    //TODO
    private Long id;
    private int post_time ;
    private String description;
    private String company;
    private String apply_url;
    private String from_url;

    public Job(Long id,
                   int post_time,
                   String description,
                   String company,
                   String apply_url,
                   String from_url) {
        this.id = id;
        this.post_time = post_time || null;
        this.description = description || '';
        this.company = company || '';
        this.apply_url = apply_url || null;
        this.from_url = from_url || null;

    }

    public Job() {

    }
    // TODO


    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", dob=" + dob +
                ", age=" + age +
                '}';
    }
}
