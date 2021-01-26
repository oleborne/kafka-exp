package com.spike.kafkaexp.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Value
// this is the way Jackson and Lombok builder can work together (see https://www.thecuriousdev.org/lombok-builder-with-jackson/)
@JsonDeserialize(builder = MessageB.MessageBBuilder.class)
@Builder(builderClassName = "MessageBBuilder", toBuilder = true)
public class MessageB {
    public String content;

    @JsonPOJOBuilder(withPrefix = "")
    public static class MessageBBuilder {
    }
}
