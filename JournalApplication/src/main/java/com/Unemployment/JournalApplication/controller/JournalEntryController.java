package com.Unemployment.JournalApplication.controller;

import com.Unemployment.JournalApplication.entity.JournalEntry;
import com.Unemployment.JournalApplication.entity.Users;
import com.Unemployment.JournalApplication.service.JournalEntryService;
import com.Unemployment.JournalApplication.service.UserEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/_journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserEntryService userEntryService;

    @GetMapping
    public ResponseEntity<?> getAllJournalEntries(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Users user = userEntryService.findByUserName(userName);
        List<JournalEntry> all = user.getJournalEntries();
        if(all!=null && !all.isEmpty()){
            return new ResponseEntity<>(all,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
       try {
           journalEntryService.saveEntry(myEntry,userName);
           return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
       }
       catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping ("id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryByID(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Users user = userEntryService.findByUserName(userName);
        //user.getJournalEntries().forEach(entry -> {
        //    System.out.println("Comparing: " + entry.getId() + " with " + myId);});
        List<JournalEntry> collect = user.getJournalEntries().stream()
                .filter(x -> x.getId().toString().equals(myId.toString()))
                .toList();
        if(!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
            if (journalEntry.isPresent()) {
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping ("id/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Boolean removed = journalEntryService.deleteById(myId,userName);
        if(removed){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("id/{myId}")
    public ResponseEntity<?> updateJournalById(@PathVariable ObjectId myId, @RequestBody JournalEntry newEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Users user = userEntryService.findByUserName(userName);

        // Find the entry within the user's list directly
        Optional<JournalEntry> journalEntryMatch = user.getJournalEntries().stream()
                .filter(x -> x.getId().equals(myId)) // Ensure both are ObjectId type
                .findFirst();

        if (journalEntryMatch.isPresent()) {
            JournalEntry oldEntry = journalEntryMatch.get();

            // Update fields if provided
            if (newEntry.getContent() != null && !newEntry.getContent().isEmpty()) {
                oldEntry.setContent(newEntry.getContent());
            }
            if (newEntry.getName() != null && !newEntry.getName().isEmpty()) {
                oldEntry.setName(newEntry.getName());
            }

            journalEntryService.saveEntry(oldEntry);
            return new ResponseEntity<>(oldEntry, HttpStatus.OK);
        }

        // If we reach here, either the entry doesn't exist or doesn't belong to this user
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
