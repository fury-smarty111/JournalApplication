package com.Unemployment.JournalApplication.Cache;

import com.Unemployment.JournalApplication.entity.ConfigJournalAppEntity;
import com.Unemployment.JournalApplication.repository.ConfigJournalAppRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AppCache {

    public enum Keys {
        WEATHER_API;
    }

    @Autowired
    private ConfigJournalAppRepository configJournalAppRepository;

    // Using ConcurrentHashMap to prevent concurrency issues during reads/writes
    private Map<String, String> cache;

    @PostConstruct
    public void init() {
        cache = new ConcurrentHashMap<>();
        List<ConfigJournalAppEntity> all = configJournalAppRepository.findAll();
        for (ConfigJournalAppEntity configJournalAppEntity : all) {
            cache.put(configJournalAppEntity.getKey(), configJournalAppEntity.getValue());
        }
    }

    public String get(String key) {
        return cache.get(key);
    }

    public void refreshCache() {
        init(); // Simply re-run the init logic
    }
}