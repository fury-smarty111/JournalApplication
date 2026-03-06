package com.Unemployment.JournalApplication.service;

import com.Unemployment.JournalApplication.entity.JournalEntry;
import com.Unemployment.JournalApplication.entity.Users;
import com.Unemployment.JournalApplication.repository.JournalEntryRepo;
import com.Unemployment.JournalApplication.sentiments.Sentiment;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; // Changed from @Component to @Service
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepo journalEntryRepo;

    @Autowired
    private UserEntryService userEntryService;

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName) {
        try {
            Users user = userEntryService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());

            // In a real app, you'd do: journalEntry.setSentiment(sentimentAnalysisService.analyze(journalEntry.getContent()));
            if (journalEntry.getSentiment() == null) {
                journalEntry.setSentiment(Sentiment.HAPPY);
            }
            JournalEntry saved = journalEntryRepo.save(journalEntry);
            user.getJournalEntries().add(saved);
            userEntryService.saveUser(user);
        } catch (Exception e) {
            // Log the error so you don't fly blind
            System.out.println("Error saving entry for user " + userName + ": " + e.getMessage());
            throw new RuntimeException("An error occurred while saving journal entry", e);
        }
    }

    // This method is usually for simple updates
    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepo.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepo.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepo.findById(id);
    }

    @Transactional // Added transactional here as well since it involves two DB operations
    public Boolean deleteById(ObjectId id, String userName) {
        boolean removed = false;
        try {
            Users user = userEntryService.findByUserName(userName);
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if (removed) {
                userEntryService.saveUser(user);
                journalEntryRepo.deleteById(id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error deleting entry: " + e.getMessage(), e);
        }
        return removed;
    }
}