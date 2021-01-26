package com.spike.kafkaexp.service;

import org.springframework.stereotype.Component;

@Component
public class TaskProcessor {
    public void processPayload(String payload) {
        throw new RuntimeException("I will not process that payload");
    }
}
