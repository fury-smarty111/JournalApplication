package com.Unemployment.JournalApplication.service;

import com.Unemployment.JournalApplication.entity.Users;
import com.Unemployment.JournalApplication.repository.UserEntryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // Recommended over @Component
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserEntryRepo userEntryRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userEntryRepo.findByUserName(username);

        if (user != null) {
            // Null-safe role handling
            String[] roles = user.getRoles() != null ? user.getRoles().toArray(new String[0]) : new String[0];

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUserName())
                    .password(user.getPassword()) // Ensure this is already Bcrypt encoded in DB
                    .roles(roles)
                    .build();
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}