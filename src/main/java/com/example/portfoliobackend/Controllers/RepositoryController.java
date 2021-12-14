package com.example.portfoliobackend.Controllers;

import com.example.portfoliobackend.ApplicationContextProvider;
import com.example.portfoliobackend.Services.RepositoryDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/repos")
public class RepositoryController {

    RepositoryDataService repositoryDataService = ApplicationContextProvider.getApplicationContext()
            .getBean(RepositoryDataService.class);

    @GetMapping
    Map<String, Integer> getLineCounts() {
        return repositoryDataService.getLineCounts();
    }

    //Testing url
    @GetMapping("/date")
    String getDate() {
        return repositoryDataService.getDate();
    }
}
