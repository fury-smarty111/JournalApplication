package com.Unemployment.JournalApplication.service;

import com.Unemployment.JournalApplication.entity.JournalEntry;
import com.Unemployment.JournalApplication.entity.Users;
import com.Unemployment.JournalApplication.repository.JournalEntryRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {


    @Autowired
    private JournalEntryRepo journalEntryRepo;

    @Autowired
    private UserEntryService userEntryService;

   @Transactional
    public void saveEntry(JournalEntry journalEntry,String userName){
        try {
            Users user = userEntryService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved= journalEntryRepo.save(journalEntry);
            user.getEntries().add(saved);
            userEntryService.saveUser(user);
        }
        catch (Exception e){
            throw new RuntimeException("An error occured while saving journal entry", e);
        }
    }

    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepo.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepo.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id){
        return journalEntryRepo.findById(id);
    }

    public Boolean deleteById(ObjectId id, String userName){
        Boolean removed = false;
       try {
           Users user = userEntryService.findByUserName(userName);
            removed = user.getEntries().removeIf(x -> x.getId().equals(id));
           if (removed) {
               userEntryService.saveUser(user);
               journalEntryRepo.deleteById(id);
           }
       }
       catch (Exception e){
           System.out.println(e);
           throw new RuntimeException("User has no such entry",e);
       }
       return  removed;
    }



}
