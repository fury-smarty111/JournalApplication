package com.Unemployment.JournalApplication;

import com.Unemployment.JournalApplication.entity.JournalEntry;
import com.Unemployment.JournalApplication.entity.Users;
import com.Unemployment.JournalApplication.repository.UserEntryRepo;
import com.Unemployment.JournalApplication.service.UserEntryService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class userServiceTests {

    @Autowired
    UserEntryRepo userEntryRepo;

    @Autowired
    UserEntryService userEntryService;

    @Disabled
    @ParameterizedTest
    @CsvSource({
            "1,2,3",
            "3,6,9",
            "1,2,4"
    })
    public void CheckExpected(int a, int b, int expected){
        assertTrue(a+b==expected,"a+b != expected value");
    }

    @Disabled
    @ParameterizedTest
    @CsvSource({
            "RAM",
            "Vim",
            "Vipul"
    })
    public void CheckUser(String userName){
        assertNotNull(userEntryRepo.findByUserName(userName));
    }

    @Disabled
    @ParameterizedTest
    @CsvSource({
            "RAM",
            "Vipul"
    })
    public void CheckUserEntries(String userName){
        Users user = userEntryRepo.findByUserName(userName);
        List<JournalEntry> journalEntries = user.getEntries();
        assertFalse(journalEntries.isEmpty(), "No entries found for user: " + userName);
        assertTrue(user.getEntries().size()>0,"no entries for the user : "+userName );
    }
}
