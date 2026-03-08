package com.Unemployment.JournalApplication.repository;

import com.Unemployment.JournalApplication.entity.Users;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntryRepo extends MongoRepository<Users, ObjectId> ,UserRepository {

    // Correctly finds a user by the indexed username field
    Users findByUserName(String username);

    // Changed to void to avoid potential casting issues during deletion
    void deleteByUserName(String username);
}