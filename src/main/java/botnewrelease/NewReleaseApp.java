package botnewrelease.controller;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NewReleaseApp {

    private static final Logger LOGGER = Logger.getLogger(NewReleaseApp.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(NewReleaseApp.class, args);
    }
}
