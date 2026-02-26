package ru.cinimex.notificationservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import ru.cinimex.notificationservice.dto.KafkaNotificationMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    private final String FROM_EMAIL = "nail.galeev.work@mail.com";
    private KafkaNotificationMessage messageDto;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "fromEmail", FROM_EMAIL);

        messageDto = new KafkaNotificationMessage();
        messageDto.setEmail("receiver@example.com");
        messageDto.setTitle("Test Subject");
        messageDto.setDescription("Test Content");
    }

    @Test
    void shouldSendEmailSuccessfully() {
        // WHEN
        emailService.sendSimpleEmail(messageDto);

        // THEN
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals(FROM_EMAIL, sentMessage.getFrom());
        assertEquals(messageDto.getEmail(), sentMessage.getTo()[0]);
        assertEquals(messageDto.getTitle(), sentMessage.getSubject());
    }

    @Test
    void shouldHandleAuthenticationException() {
        // Имитируем ошибку пароля приложения
        doThrow(new MailAuthenticationException("Authentication failed"))
                .when(mailSender).send(any(SimpleMailMessage.class));

        // Проверяем, что метод НЕ выбрасывает исключение наружу (так как есть try-catch)
        assertDoesNotThrow(() -> emailService.sendSimpleEmail(messageDto));

        // Проверяем, что попытка вызова была
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void shouldHandleGeneralMailException() {
        // Имитируем общую ошибку (например, сервер недоступен)
        doThrow(new MailSendException("SMTP server unreachable"))
                .when(mailSender).send(any(SimpleMailMessage.class));

        assertDoesNotThrow(() -> emailService.sendSimpleEmail(messageDto));

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void shouldHandleUnexpectedException() {
        // Имитируем странную ошибку, например NullPointerException
        doThrow(new RuntimeException("Unexpected runtime error"))
                .when(mailSender).send(any(SimpleMailMessage.class));

        assertDoesNotThrow(() -> emailService.sendSimpleEmail(messageDto));

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}