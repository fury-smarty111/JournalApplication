package com.Unemployment.JournalApplication.repository;

import com.Unemployment.JournalApplication.entity.JournalEntry;
import com.Unemployment.JournalApplication.entity.Users;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserEntryRepo extends MongoRepository<Users, ObjectId> {

    Users findByUserName(String username);

    Users deleteByUserName(String username);
}
