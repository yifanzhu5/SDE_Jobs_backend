package com.example.demo.job;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.sql.Timestamp;
import java.util.List;

//@Configuration
//public class JobConfig {

    //@Bean
//    CommandLineRunner commandLineRunner(JobRepository repository) {
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        return args -> {
//            Job software1 = new Job(
//                    "Front-end Developer",
//                    "Kitchener",
//                    "Google",
//                    timestamp,
//                    "job description here",
//                    "https://www.google.com/",
//                    "https://www.google.com/"
//            );
//
//            Job software2 = new Job(
//                    "Back-end Developer",
//                    "Seattle",
//                    "Facebook",
//                    timestamp,
//                    "new grad",
//                    "https://www.facebook.com/",
//                    "https://www.facebook.com/"
//            );
//
//            repository.saveAll(List.of(software1, software2));
//        };
//    }
//}
