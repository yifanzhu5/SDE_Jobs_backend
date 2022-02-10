package com.example.demo.job;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Job")//default is the actual class name
@Table(
        name="jobs",//job
        uniqueConstraints = {
                @UniqueConstraint(name="from_url_unique",columnNames = "from_url")
        }
)
public class Job {
    @Id
    @SequenceGenerator(
            name = "job_sequence",
            sequenceName = "job_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "job_sequence"
    )

    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "title",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String title;

    @Column(
            name = "locations",
            nullable = true,
            columnDefinition = "TEXT"
    )
    private String locations;

    @Column(
            name = "company",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String company;

    @Column(
            name = "publish_time",
            nullable = true,
            columnDefinition = "INT"
    )
    private Integer publish_time;

    @Column(
            name = "description",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String description;

    @Column(
            name = "apply_url",
            nullable = true,
            columnDefinition = "TEXT"
    )
    private String apply_url;

    @Column(
            name = "from_url",
            nullable = true,
            columnDefinition = "TEXT"
    )
    private String from_url;

    @Column(
            name = "basic_qualifications",
            nullable = true,
            columnDefinition = "TEXT"
    )
    private String basic_qualifications;

    @Column(
            name = "city",
            nullable = true,
            columnDefinition = "TEXT"
    )
    private String city;

    @Column(
            name = "job_category",
            nullable = true,
            columnDefinition = "TEXT"
    )
    private String job_category;

    @Column(
            name = "job_family",
            nullable = true,
            columnDefinition = "TEXT"
    )
    private String job_family;

    @Column(
            name = "job_schedule_type",
            nullable = true,
            columnDefinition = "TEXT"
    )
    private String job_schedule_type;

    @Column(
            name = "preferred_qualifications",
            nullable = true,
            columnDefinition = "TEXT"
    )
    private String preferred_qualifications;

    @Column(
            name = "update_time",
            nullable = true,
            columnDefinition = "TEXT"
    )
    private String update_time;

    @Column(
            name = "new_grad",
            nullable = true,
            columnDefinition = "TEXT"
    )
    private String new_grad;

    public Job() {
    }

    public Job(String title, String locations, String company, Integer publish_time, String description, String apply_url, String from_url, String basic_qualifications, String city, String job_category, String job_family, String job_schedule_type, String preferred_qualifications, String update_time, String new_grad) {
        this.title = title;
        this.locations = locations;
        this.company = company;
        this.publish_time = publish_time;
        this.description = description;
        this.apply_url = apply_url;
        this.from_url = from_url;
        this.basic_qualifications = basic_qualifications;
        this.city = city;
        this.job_category = job_category;
        this.job_family = job_family;
        this.job_schedule_type = job_schedule_type;
        this.preferred_qualifications = preferred_qualifications;
        this.update_time = update_time;
        this.new_grad = new_grad;
    }

    public Job(Long id, String title, String locations, String company, Integer publish_time, String description, String apply_url, String from_url, String basic_qualifications, String city, String job_category, String job_family, String job_schedule_type, String preferred_qualifications, String update_time, String new_grad) {
        this.id = id;
        this.title = title;
        this.locations = locations;
        this.company = company;
        this.publish_time = publish_time;
        this.description = description;
        this.apply_url = apply_url;
        this.from_url = from_url;
        this.basic_qualifications = basic_qualifications;
        this.city = city;
        this.job_category = job_category;
        this.job_family = job_family;
        this.job_schedule_type = job_schedule_type;
        this.preferred_qualifications = preferred_qualifications;
        this.update_time = update_time;
        this.new_grad = new_grad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(Integer publish_time) {
        this.publish_time = publish_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApply_url() {
        return apply_url;
    }

    public void setApply_url(String apply_url) {
        this.apply_url = apply_url;
    }

    public String getFrom_url() {
        return from_url;
    }

    public void setFrom_url(String from_url) {
        this.from_url = from_url;
    }

    public String getBasic_qualifications() {
        return basic_qualifications;
    }

    public void setBasic_qualifications(String basic_qualifications) {
        this.basic_qualifications = basic_qualifications;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getJob_category() {
        return job_category;
    }

    public void setJob_category(String job_category) {
        this.job_category = job_category;
    }

    public String getJob_family() {
        return job_family;
    }

    public void setJob_family(String job_family) {
        this.job_family = job_family;
    }

    public String getJob_schedule_type() {
        return job_schedule_type;
    }

    public void setJob_schedule_type(String job_schedule_type) {
        this.job_schedule_type = job_schedule_type;
    }

    public String getPreferred_qualifications() {
        return preferred_qualifications;
    }

    public void setPreferred_qualifications(String preferred_qualifications) {
        this.preferred_qualifications = preferred_qualifications;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getNew_grad() {
        return new_grad;
    }

    public void setNew_grad(String new_grad) {
        this.new_grad = new_grad;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", locations='" + locations + '\'' +
                ", company='" + company + '\'' +
                ", publish_time=" + publish_time +
                ", description='" + description + '\'' +
                ", apply_url='" + apply_url + '\'' +
                ", from_url='" + from_url + '\'' +
                ", basic_qualifications='" + basic_qualifications + '\'' +
                ", city='" + city + '\'' +
                ", job_category='" + job_category + '\'' +
                ", job_family='" + job_family + '\'' +
                ", job_schedule_type='" + job_schedule_type + '\'' +
                ", preferred_qualifications='" + preferred_qualifications + '\'' +
                ", update_time='" + update_time + '\'' +
                ", new_grad='" + new_grad + '\'' +
                '}';
    }
}
