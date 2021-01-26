package com.spike.kafkaexp.listener;

import com.spike.kafkaexp.domain.MessageA;
import com.spike.kafkaexp.domain.MessageB;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.spike.kafkaexp.KafkaConfig.TASKS_TOPIC_NAME;

@Component
@Slf4j
@KafkaListener(topics = TASKS_TOPIC_NAME, groupId = "MyBackgroundProcessor", concurrency = "2")
public class MyBackgroundProcessor {

  @KafkaHandler(isDefault = true)
  public void listenToStringMessages(ConsumerRecord message) {
    log.info("Received message: " + message);
  }

  @KafkaHandler
  public void listenToMessageA(MessageA message) {
    log.info("Received message A with body: " + message.getBody());
  }

  @KafkaHandler
  public void listenToMessageB(MessageB message) {
    log.info("Received message B with content: " + message.getContent());
  }
}
