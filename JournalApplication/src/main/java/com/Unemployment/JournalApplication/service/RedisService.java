package com.Unemployment.JournalApplication.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper mapper;

    public <T> T get(String key, Class<T> entityClass) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value == null) {
                return null;
            }
            // value is likely already a JSON string if stored via the set method below
            return mapper.readValue(value.toString(), entityClass);
        } catch (Exception e) {
            log.error("Redis Get Exception for key {}: ", key, e);
            return null;
        }
    }

    public void set(String key, Object o, Long ttl) {
        try {
            String jsonValue = mapper.writeValueAsString(o);
            if (ttl != null && ttl > 0) {
                redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
            } else {
                // Save without TTL if none is provided
                redisTemplate.opsForValue().set(key, jsonValue);
            }
        } catch (Exception e) {
            log.error("Redis Set Exception for key {}: ", key, e);
        }
    }
}