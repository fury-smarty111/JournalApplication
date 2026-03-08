package com.Unemployment.JournalApplication.service;

import com.Unemployment.JournalApplication.entity.Users;
import com.Unemployment.JournalApplication.repository.JournalEntryRepo;
import com.Unemployment.JournalApplication.repository.UserEntryRepo;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserEntryService {

    @Autowired
    private UserEntryRepo userEntryRepo;

    @Autowired
    private JournalEntryRepo journalEntryRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveNewEntry(Users users) {
        try {
            if (users.getPassword() == null || users.getPassword().isEmpty()) {
                log.error("Attempted to save user {} without a password", users.getUserName());
            }
            users.setPassword(passwordEncoder.encode(users.getPassword()));
            users.setRoles(Arrays.asList("USER"));
            userEntryRepo.save(users);
        } catch (Exception e) {
            log.error("An error occurred while saving the user {}", users.getUserName(), e);
            throw new RuntimeException("Could not create user", e);
        }
    }

    public void saveAdmin(Users users) {
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setRoles(Arrays.asList("USER", "ADMIN"));
        userEntryRepo.save(users);
    }

    // Keep this for updating user profile data without re-hashing the password
    public void saveUser(Users users) {
        userEntryRepo.save(users);
    }

    public List<Users> getAll() {
        return userEntryRepo.findAll();
    }

    public Optional<Users> findById(ObjectId id) {
        return userEntryRepo.findById(id);
    }

    public void deleteById(ObjectId id) {
        userEntryRepo.deleteById(id);
    }

    public Users findByUserName(String userName) {
        return userEntryRepo.findByUserName(userName);
    }

    // Re-added this method for you
    public void deleteByUserName(String userName) {
        Users user = userEntryRepo.findByUserName(userName);
        if (user != null) {
            // 1. Delete all journal entries belonging to this user
            if (user.getJournalEntries() != null && !user.getJournalEntries().isEmpty()) {
                journalEntryRepo.deleteAll(user.getJournalEntries());
            }
            // 2. Delete the user
            userEntryRepo.deleteByUserName(userName);
            log.info("User {} and all associated journal entries deleted.", userName);
        }
    }
}