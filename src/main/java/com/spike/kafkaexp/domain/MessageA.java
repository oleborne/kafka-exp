package com.spike.kafkaexp.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Value
// this is the way Jackson and Lombok builder can work together (see https://www.thecuriousdev.org/lombok-builder-with-jackson/)
@JsonDeserialize(builder = MessageA.MessageABuilder.class)
@Builder(builderClassName = "MessageABuilder", toBuilder = true)
public class MessageA {
    public String body;

    @JsonPOJOBuilder(withPrefix = "")
    public static class MessageABuilder {
    }
}
