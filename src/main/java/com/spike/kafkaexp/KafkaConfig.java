package com.spike.kafkaexp;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaConsumerFactoryCustomizer;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;

@Configuration
@Slf4j
public class KafkaConfig {

  public static final String TASKS_TOPIC_NAME = "tasks";
  public static final String BACKOFF_1M_TOPIC_NAME = "tasks-backoff-1m";
  public static final String BACKOFF_3M_TOPIC_NAME = "tasks-backoff-3m";
  public static final String BACKOFF_5M_TOPIC_NAME = "tasks-backoff-5m";
  public static final short REPLICATION_FACTOR = (short) 3;
  public static final int NUM_PARTITIONS = 5;

  @Bean
  public NewTopic backgroundTasksTopic() {
    return new NewTopic(TASKS_TOPIC_NAME, NUM_PARTITIONS, REPLICATION_FACTOR);
  }

  @Bean
  public NewTopic backoff1mTasksTopic() {
    return new NewTopic(BACKOFF_1M_TOPIC_NAME, NUM_PARTITIONS, REPLICATION_FACTOR);
  }

  @Bean
  public NewTopic backoff2mTasksTopic() {
    return new NewTopic(BACKOFF_3M_TOPIC_NAME, NUM_PARTITIONS, REPLICATION_FACTOR);
  }

  @Bean
  public NewTopic backoff5mTasksTopic() {
    return new NewTopic(BACKOFF_5M_TOPIC_NAME, NUM_PARTITIONS, REPLICATION_FACTOR);
  }

  @Bean
  public Serializer kafkaValueSerializer() {
    return new JsonSerializer();
  }

  @Bean
  public Deserializer kafkaValueDeserializer() {
    return new JsonDeserializer();
  }

  @Bean
  public DefaultKafkaProducerFactoryCustomizer myProducerFactoryCustomizer(
      Serializer kafkaValueSerializer) {
    return producerFactory -> {
      kafkaValueSerializer.configure(producerFactory.getConfigurationProperties(), false);
      producerFactory.setValueSerializer(kafkaValueSerializer);
    };
  }

  @Bean
  public DefaultKafkaConsumerFactoryCustomizer myConsumerFactoryCustomizer(
      Deserializer kafkaValueDeserializer) {
    return consumerFactory -> {
      kafkaValueDeserializer.configure(consumerFactory.getConfigurationProperties(), false);
      consumerFactory.setValueDeserializer(kafkaValueDeserializer);
    };
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String>
      myStringKafkaListenerContainerFactory(ConsumerFactory<?, ?> consumerFactory) {
    HashMap<String, Object> props = new HashMap<>(consumerFactory.getConfigurationProperties());

    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

    ConcurrentKafkaListenerContainerFactory<String, String> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(props));
    return factory;
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String>
      myManualAckListenerContainerFactory(ConsumerFactory<?, ?> consumerFactory) {
    HashMap<String, Object> props = new HashMap<>(consumerFactory.getConfigurationProperties());

    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

    ConcurrentKafkaListenerContainerFactory<String, String> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(props));
    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
    return factory;
  }


}
