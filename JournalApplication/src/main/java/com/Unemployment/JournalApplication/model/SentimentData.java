package com.Unemployment.JournalApplication.model;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value // Makes the class immutable (final fields, no setters)
@Builder
@Jacksonized // Helps Jackson handle deserialization for immutable @Value classes
public class SentimentData {

    String email;
    String sentiment;

}