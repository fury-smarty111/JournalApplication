package com.Unemployment.JournalApplication.controller;

import com.Unemployment.JournalApplication.entity.Users;
import com.Unemployment.JournalApplication.service.UserEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserEntryController {

    @Autowired
    private UserEntryService userEntryService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody Users user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Users userInDB = userEntryService.findByUserName(currentUsername);

        if (userInDB != null) {
            // Only update if the new value is not null/empty
            if (user.getUserName() != null && !user.getUserName().isEmpty()) {
                userInDB.setUserName(user.getUserName());
            }
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                userInDB.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                userInDB.setEmail(user.getEmail());
            }
            userInDB.setSentimentAnalysis(user.isSentimentAnalysis());

            userEntryService.saveUser(userInDB);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        userEntryService.deleteByUserName(username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 is standard for successful delete
    }
}