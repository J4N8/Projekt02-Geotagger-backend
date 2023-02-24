package me.j4n8.projekt02backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Projekt02BackendApplication {

    private static final Logger log = LoggerFactory.getLogger(Projekt02BackendApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(Projekt02BackendApplication.class, args);
    }
}
