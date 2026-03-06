package com.Unemployment.JournalApplication.repository;

import com.Unemployment.JournalApplication.entity.ConfigJournalAppEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository // Optional but good for clarity
public interface ConfigJournalAppRepository extends MongoRepository<ConfigJournalAppEntity, ObjectId> {

    // Allows you to find specific config items like "WEATHER_API" directly
    ConfigJournalAppEntity findByKey(String key);
}