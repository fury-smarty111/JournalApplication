package com.Unemployment.JournalApplication.controller;

import com.Unemployment.JournalApplication.DTO.UserDTO;
import com.Unemployment.JournalApplication.Utilis.JwtUtil;
import com.Unemployment.JournalApplication.entity.Users;
import com.Unemployment.JournalApplication.service.UserDetailServiceImpl;
import com.Unemployment.JournalApplication.service.UserEntryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
@Tag(name = "Public APIs")
public class PublicController {

    @Autowired
    private UserEntryService userEntryService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Ok";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDTO user) {
        try {
            Users newUser = new Users();
            newUser.setEmail(user.getEmail());
            newUser.setUserName(user.getUserName());
            newUser.setPassword(user.getPassword());
            newUser.setSentimentAnalysis(user.isSentimentAnalysis());
            userEntryService.saveNewEntry(newUser);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Users user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
            UserDetails userDetails = userDetailService.loadUserByUsername(user.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception occurred while createAuthenticationToken ", e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }
}