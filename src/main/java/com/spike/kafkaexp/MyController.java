package com.spike.kafkaexp;

import com.spike.kafkaexp.domain.MessageA;
import com.spike.kafkaexp.domain.MessageB;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.util.function.Function;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MyController {

  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final NewTopic backgroundTasksTopic;

  @PostMapping("/hello")
  public Mono<String> publishMessage(@RequestBody Mono<String> input) {
    return input.flatMap(processContent(Function.<String>identity()));
  }

  @PostMapping("/message-a")
  public Mono<String> publishMessageA(@RequestBody Mono<String> input) {
    return input.flatMap(processContent(content -> MessageA.builder().body(content).build()));
  }

  @PostMapping("/message-b")
  public Mono<String> publishMessageB(@RequestBody Mono<String> input) {
    return input.flatMap(
        processContent(content -> MessageB.builder().content(content).build()));
  }

  private Function<String, ? extends Mono<? extends String>> processContent(
      Function<String, ? extends Object> payloadCreator) {
    return content ->
        Mono.create(
            sink ->
                kafkaTemplate
                    .send(backgroundTasksTopic.name(), payloadCreator.apply(content))
                    .addCallback(successHandler(sink), sink::error));
  }

  private SuccessCallback<SendResult<String, Object>> successHandler(MonoSink<String> sink) {
    return result -> {
      String msg = "Sent msg " + result;
      log.info(msg);
      sink.success(msg);
    };
  }
}
