package com.spike.kafkaexp.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.spike.kafkaexp.KafkaConfig.TASKS_TOPIC_NAME;

@Component
@KafkaListener(topics = TASKS_TOPIC_NAME, groupId = "DebugBackgroundProcessor")
@Slf4j
public class DebugBackgroundProcessor {
    @KafkaHandler(isDefault = true)
    public void inspectMessage(ConsumerRecord record) {
        log.info("Inspecting record: " + record.toString());
    }
}
