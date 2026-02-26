package ru.cinimex.notificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.cinimex.notificationservice.dto.KafkaNotificationMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendSimpleEmail(KafkaNotificationMessage kafkaMessage) {
        try {
            log.info("Попытка отправки письма ОТ: {} НА: {}", fromEmail, kafkaMessage.getEmail());

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(kafkaMessage.getEmail());
            message.setSubject(kafkaMessage.getTitle());
            message.setText(kafkaMessage.getDescription());

            mailSender.send(message);
            log.info("Email успешно отправлен на {}", kafkaMessage.getEmail());

        } catch (org.springframework.mail.MailAuthenticationException e) {
            log.error("Ошибка авторизации на почтовом сервере (проверьте пароль приложения): {}", e.getMessage());
        } catch (org.springframework.mail.MailException e) {
            log.error("Произошла ошибка при отправке письма на {}: {}", kafkaMessage.getEmail(), e.getMessage());
        } catch (Exception e) {
            log.error("Непредвиденная ошибка в EmailService: ", e);
        }
    }
}
