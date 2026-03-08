package com.Unemployment.JournalApplication.service;

import com.Unemployment.JournalApplication.model.SentimentData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SentimentConsumerService {

    @Autowired
    private EmailService emailService;

    /**
     * This method automatically triggers whenever a message is pushed
     * to the "weekly-sentiments" topic in Kafka.
     */
    @KafkaListener(topics = "weekly-sentiments", groupId = "sentiment-group")
    public void consumeSentimentData(SentimentData sentimentData) {
        try {
            if (sentimentData != null && sentimentData.getEmail() != null) {
                log.info("Received sentiment data for: {}", sentimentData.getEmail());

                emailService.sendEmail(
                        sentimentData.getEmail(),
                        "Weekly Sentiment Analysis Report",
                        "Hello! Based on your journals from the last 7 days, your most frequent mood was: " + sentimentData.getSentiment()
                );

                log.info("Email successfully sent to {}", sentimentData.getEmail());
            }
        } catch (Exception e) {
            log.error("Failed to process sentiment data for Kafka message", e);
        }
    }
}