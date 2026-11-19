package com.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Starts the Spring Boot library application.
@SpringBootApplication
public class LibraryApplication {

    // Launches the embedded web server and database-backed application.
    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }
}
