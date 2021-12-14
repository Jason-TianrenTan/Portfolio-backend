package com.example.portfoliobackend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Scope("application")
public class RepositoryDataService {

    private final Map<String, Integer> lineCounts;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;

    @Autowired
    public RepositoryDataService() {
        this.lineCounts = new HashMap<>();
    }

    public void mergeLineCounts(Map<String, Integer> map) {
        map.forEach((key, value) -> this.lineCounts.merge(key, value, (v1, v2) -> v1 + v2));
    }

    public Map<String, Integer> getLineCounts() {
        return lineCounts;
    }


}
