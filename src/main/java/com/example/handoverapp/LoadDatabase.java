package com.example.handoverapp;

import com.example.handoverapp.entity.Doctor;
import com.example.handoverapp.entity.Patient;
import com.example.handoverapp.entity.Task;
import com.example.handoverapp.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

// Uncomment this code to inject some mock data into the database so that Json can be examined straight away when manually testing
//
//    @Bean
//    CommandLineRunner initDatabase(TaskRepository repository) {
//        return args -> {
//            log.info("Preloaded data"+ repository.save(new Task()));
//            log.info("Preloaded data"+ repository.save(new Task()));
//        };
//    }
}