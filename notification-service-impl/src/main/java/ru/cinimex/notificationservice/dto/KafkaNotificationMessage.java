package ru.cinimex.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KafkaNotificationMessage {
    private String email;

    @JsonAlias("header")
    private String title;

    @JsonAlias("body")
    private String description;
}