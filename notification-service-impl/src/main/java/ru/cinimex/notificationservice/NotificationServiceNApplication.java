package ru.cinimex.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class NotificationServiceNApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceNApplication.class, args);
    }

}
