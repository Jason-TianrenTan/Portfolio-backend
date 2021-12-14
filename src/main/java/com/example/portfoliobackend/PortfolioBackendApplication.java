package com.example.portfoliobackend;

import com.example.portfoliobackend.Services.DateFeedService;
import com.example.portfoliobackend.Services.GithubFeedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class PortfolioBackendApplication {

    private static final Logger log = LoggerFactory.getLogger(PortfolioBackendApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PortfolioBackendApplication.class, args);
    }

    @Value("${repo_url}")
    private String repo_url;

    @Value("${date_url}")
    private String date_url;

    @Scheduled(fixedRate = 24 * 3600 * 1000)
    public void fetchGithubData() {
        log.info("Fetching repo data");
        log.info("Accessing Remote Repositories from " + repo_url);
        GithubFeedService githubFeed = ApplicationContextProvider.getApplicationContext().getBean(GithubFeedService.class);
        githubFeed.getRepoStats(repo_url);
    }

//    @Scheduled(fixedRate = 5000)
//    public void fetchDate() {
//        log.info("Fetching date");
//        DateFeedService dateFeedService = ApplicationContextProvider.getApplicationContext().getBean(DateFeedService.class);
//        dateFeedService.getDate(date_url);
//    }

}
