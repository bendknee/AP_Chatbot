package advprog.weather.bot.controller;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WeatherApplication {

    private static final Logger LOGGER = Logger.getLogger(WeatherApplication.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(WeatherApplication.class, args);
    }
}
