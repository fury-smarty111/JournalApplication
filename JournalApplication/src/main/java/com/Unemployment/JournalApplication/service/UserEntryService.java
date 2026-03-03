package com.Unemployment.JournalApplication.service;

import com.Unemployment.JournalApplication.entity.Users;
import com.Unemployment.JournalApplication.repository.UserEntryRepo;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserEntryService {


    @Autowired
    private UserEntryRepo userEntryRepo;

  //  private static final Logger logger = LoggerFactory.getLogger(UserEntryService.class);


    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public void saveNewEntry(Users users){
        try{
            if (users.getPassword() == null || users.getPassword().isEmpty()) {
                log.error("Attempted to save user {} without a password", users.getUserName());
            }
            users.setPassword(passwordEncoder.encode(users.getPassword()));
            users.setRoles(Arrays.asList("USER"));
            userEntryRepo.save(users);
        }

        catch (Exception e){
           // log.info("HAHAHAHA");
            log.error("An error occured while saving the user {}",users.getUserName(),e);
            //log.warn("hahah");
           // log.trace("traceee");
            //log.debug("debuggg");
            throw  new RuntimeException("Could not create user");
        }
    }
    public void saveAdmin(Users users){
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setRoles(Arrays.asList("USER","ADMIN"));
        userEntryRepo.save(users);
    }

    public void saveUser(Users users){
        userEntryRepo.save(users);
    }

    public List<Users> getAll() {
        return userEntryRepo.findAll();
    }

    public Optional<Users> findById(ObjectId id){
        return userEntryRepo.findById(id);
    }

    public void deleteById(ObjectId id){
        userEntryRepo.deleteById(id);
    }

    public Users findByUserName(String userName){
        return userEntryRepo.findByUserName(userName);
    }

    public void deleteByUserName(String userName){
         userEntryRepo.deleteByUserName(userName);
    }


}
