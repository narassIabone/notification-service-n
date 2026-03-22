package ru.cinimex.notificationservice.consumer;

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

    @KafkaListener(
            topics = "${app.kafka.topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(KafkaNotificationMessage message) {
        log.info("Получено сообщение из Kafka для: {}", message.getEmail());
        emailService.sendSimpleEmail(message);
    }
}
