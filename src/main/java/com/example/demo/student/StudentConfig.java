package com.example.demo.student;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class StudentConfig {

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository){
     return args -> {
          Student gabriel = new Student(
                 "Gabriel",
                 "gabriel@gabriel.com",
                 LocalDate.of(2000, Month.AUGUST, 4)
         );

         Student pedro = new Student(
                 "pedro",
                 "pedro@pedro.com",
                 LocalDate.of(1999, Month.JANUARY, 10)
         );

         repository.saveAll(
                 List.of(gabriel, pedro)
         );
     };
    }
}
