package com.Unemployment.JournalApplication.controller;

import com.Unemployment.JournalApplication.entity.Users;
import com.Unemployment.JournalApplication.service.UserEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserEntryService userEntryService;

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers(){
        List<Users> all = userEntryService.getAll();
        if(all!=null && !all.isEmpty()){
            return new ResponseEntity<>(all,HttpStatus.OK);
        }
       return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-adminUser")
    public void createAdminUser(@RequestBody Users User){
        userEntryService.saveAdmin(User);
    }
}
