package com.spike.kafkaexp.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import static com.spike.kafkaexp.KafkaConfig.TASKS_TOPIC_NAME;

@Component
@KafkaListener(topics = TASKS_TOPIC_NAME, groupId = "StringBackgroundProcessor", containerFactory = "myStringKafkaListenerContainerFactory")
@Slf4j
public class StringBackgroundProcessor
{
    @KafkaHandler(isDefault = true)
    public void inspectMessage(String payload, MessageHeaders headers) {
        log.info("Handling string payload: {}", payload);
        log.info("With headers: {}", headers);
    }
}
