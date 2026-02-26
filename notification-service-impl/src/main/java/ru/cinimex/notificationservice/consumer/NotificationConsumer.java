package ru.cinimex.notificationservice.consumer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.cinimex.notificationservice.dto.KafkaNotificationMessage;
import ru.cinimex.notificationservice.service.EmailService;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final EmailService emailService;

    @PostConstruct
    public void init() {
        log.info("NotificationConsumer бин успешно создан и инициализирован с новыми настройками!");
    }

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(KafkaNotificationMessage message) {
        log.info("Получено сообщение из Kafka для: {}", message.getEmail());
        emailService.sendSimpleEmail(message);
    }
}
