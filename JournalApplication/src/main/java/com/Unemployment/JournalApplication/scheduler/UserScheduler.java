package com.Unemployment.JournalApplication.scheduler;

import com.Unemployment.JournalApplication.entity.JournalEntry;
import com.Unemployment.JournalApplication.entity.Users;
import com.Unemployment.JournalApplication.model.SentimentData;
import com.Unemployment.JournalApplication.repository.UserEntryRepo; // Use the Repo Interface
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private UserEntryRepo userRepository;

    @Autowired
    private KafkaTemplate<String, SentimentData> kafkaTemplate;

    @Scheduled(cron = "0 0 9 * * SUN") // Every Sunday at 9:00 AM
    public void fetchUsersAndSendSaMail() {
        List<Users> users = userRepository.getUserForSA();

        for (Users user : users) {
            List<JournalEntry> journalEntries = user.getJournalEntries();

            // 1. Filter and Extract Sentiments
            List<String> sentiments = journalEntries.stream()
                    .filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                    .map(x -> x.getSentiment().toString())
                    .filter(s -> s != null)
                    .collect(Collectors.toList());

            // 2. Count Occurrences
            Map<String, Integer> sentimentCounts = new HashMap<>();
            for (String sentiment : sentiments) {
                sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
            }

            // 3. Find Most Frequent
            String mostFrequentSentiment = null;
            int maxCount = 0;
            for (Map.Entry<String, Integer> entry : sentimentCounts.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }

            // 4. PUSH TO KAFKA
            if (mostFrequentSentiment != null && user.getEmail() != null) {
                SentimentData sentimentData = SentimentData.builder()
                        .email(user.getEmail())
                        .sentiment(mostFrequentSentiment)
                        .build();

                // Sending to a topic named "weekly-sentiments"
                kafkaTemplate.send("weekly-sentiments", sentimentData.getEmail(), sentimentData);
            }
        }
    }
}