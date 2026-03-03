package com.Unemployment.JournalApplication.controller;

import com.Unemployment.JournalApplication.entity.Users;
import com.Unemployment.JournalApplication.service.UserEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
public class publicController {

    @Autowired
    private UserEntryService userEntryService;

    @GetMapping("/health-check")
    public String HealthCheck(){
        return "Ok";
    }

    @GetMapping
    public List<Users> getAllUsers(){
        return userEntryService.getAll();
    }

    @PostMapping("/create-user")
    public void createUser(@RequestBody Users users){
        userEntryService.saveNewEntry(users);
    }
}
