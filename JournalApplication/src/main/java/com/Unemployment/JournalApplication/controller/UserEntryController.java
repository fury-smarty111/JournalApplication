package com.Unemployment.JournalApplication.controller;

import com.Unemployment.JournalApplication.entity.Users;
import com.Unemployment.JournalApplication.service.UserEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserEntryController {

   @Autowired
   private UserEntryService userEntryService;



   @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody Users users){
       Authentication authentication=  SecurityContextHolder.getContext().getAuthentication();
       String username = authentication.getName();
      Users userinDB= userEntryService.findByUserName(username);
          userinDB.setUserName(users.getUserName());
          userinDB.setPassword(users.getPassword());
          userEntryService.saveNewEntry(userinDB);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
   }

   @DeleteMapping
    public ResponseEntity<?> deleteUserById(){
       Authentication authentication=  SecurityContextHolder.getContext().getAuthentication();
       String username = authentication.getName();
       userEntryService.deleteByUserName(username);
       return new ResponseEntity<>(HttpStatus.OK);
   }

}

