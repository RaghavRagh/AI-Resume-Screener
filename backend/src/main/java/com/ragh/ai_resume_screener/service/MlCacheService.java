package com.ragh.ai_resume_screener.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class MlCacheService {

    private final Map<String, Double> similarityCache = new ConcurrentHashMap<>();

    public Double get(String key) {
        return similarityCache.get(key);
    }

    public void put(String key, Double val) {
        similarityCache.put(key, val);
    }
}
