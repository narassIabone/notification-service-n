package ru.cinimex.notificationservice.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.cinimex.notificationservice.dto.KafkaNotificationMessage;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public ConsumerFactory<String, KafkaNotificationMessage> consumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties();

        JsonDeserializer<KafkaNotificationMessage> jsonDeserializer = new JsonDeserializer<>(KafkaNotificationMessage.class, false);
        jsonDeserializer.addTrustedPackages("*");

        ErrorHandlingDeserializer<KafkaNotificationMessage> errorHandlingDeserializer =
                new ErrorHandlingDeserializer<>(jsonDeserializer);

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                errorHandlingDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaNotificationMessage> kafkaListenerContainerFactory(
            ConsumerFactory<String, KafkaNotificationMessage> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, KafkaNotificationMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}