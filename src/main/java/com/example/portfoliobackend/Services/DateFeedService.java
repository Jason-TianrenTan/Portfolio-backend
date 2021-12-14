package com.example.portfoliobackend.Services;

import com.example.portfoliobackend.ApplicationContextProvider;
import com.example.portfoliobackend.Models.RepositoryBean;
import com.example.portfoliobackend.Models.WorldClockBean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class DateFeedService {

    private final WebClient webClient;

    public DateFeedService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    RepositoryDataService repositoryDataService = ApplicationContextProvider.getApplicationContext()
            .getBean(RepositoryDataService.class);

    public void getDate(String url) {
        this.webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(WorldClockBean.class)
                .subscribe(result -> repositoryDataService.setDate(Long.toString(result.getCurrentFileTime())));
    }
}
