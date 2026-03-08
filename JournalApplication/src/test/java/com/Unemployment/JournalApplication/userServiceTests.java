package com.Unemployment.JournalApplication;

import com.Unemployment.JournalApplication.entity.JournalEntry;
import com.Unemployment.JournalApplication.entity.Users;
import com.Unemployment.JournalApplication.repository.UserEntryRepo;
import com.Unemployment.JournalApplication.service.UserEntryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class userServiceTests { // Renamed to UpperCamelCase

    @Autowired
    private UserEntryRepo userEntryRepo;

    @Autowired
    private UserEntryService userEntryService;

    /**
     * Simple math test to verify Parameterized Test setup.
     */
    @ParameterizedTest
    @CsvSource({
            "1, 2, 3",
            "3, 6, 9"
    })
    public void checkExpected(int a, int b, int expected) {
        assertEquals(expected, a + b, "The sum of a and b should equal the expected value");
    }

    /**
     * Verifies that specific users exist in the database.
     * Ensure these users exist in your MongoDB before running.
     */
    @ParameterizedTest
    @CsvSource({
            "RAM",
            "Vipul"
    })
    public void checkUserExists(String userName) {
        Users user = userEntryRepo.findByUserName(userName);
        assertNotNull(user, "User " + userName + " was not found in the database");
    }

    /**
     * Verifies that the user has at least one journal entry associated with their account.
     */
    @ParameterizedTest
    @CsvSource({
            "RAM",
            "Vipul"
    })
    public void checkUserEntries(String userName) {
        Users user = userEntryRepo.findByUserName(userName);

        // Check if user exists first to avoid NullPointerException
        assertNotNull(user, "Cannot check entries because user " + userName + " does not exist");

        List<JournalEntry> journalEntries = user.getJournalEntries();

        assertNotNull(journalEntries, "The journal entries list for " + userName + " should not be null");
        assertFalse(journalEntries.isEmpty(), "User " + userName + " should have at least one journal entry");
    }

    /**
     * A standard JUnit test (Non-parameterized)
     */
    @Test
    public void testRepositoryIsNotNull() {
        assertNotNull(userEntryRepo, "Spring should have injected the UserEntryRepo bean");
    }
}