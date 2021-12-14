package com.example.portfoliobackend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Scope("application")
public class RepositoryDataService {
    private Map<String, Integer> lineCounts;

    @Autowired
    public RepositoryDataService() {
        this.lineCounts = new HashMap<>();
    }

    public void setLineCounts(Map<String, Integer> map) {
        this.lineCounts = map;
    }

    public Map<String, Integer> getLineCounts() {
        return lineCounts;
    }
}
