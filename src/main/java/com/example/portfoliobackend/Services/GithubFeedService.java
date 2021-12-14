package com.example.portfoliobackend.Services;

import com.example.portfoliobackend.ApplicationContextProvider;
import com.example.portfoliobackend.Models.RepositoryBean;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

@Service
public class GithubFeedService {

    private final WebClient webClient;
    private List<String> suffixes = new ArrayList<>(Arrays.asList(".java", ".js", ".cpp"));
    private static final Logger logger = LoggerFactory.getLogger(GithubFeedService.class);
    private RepositoryDataService repositoryDataService = ApplicationContextProvider.getApplicationContext()
            .getBean(RepositoryDataService.class);

    public GithubFeedService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    public void getRepoStats(String url) {
        Mono<RepositoryBean[]> mono = this.webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(RepositoryBean[].class);

        mono.subscribe(result -> Arrays.stream(result)
                .forEach(repo -> {
                    if (!repo.getFork()) {
                        File dir = cloneRepo(repo.getHtmlUrl() + ".git", repo.getName());
                        logger.info("Walking repo...");
                        Map<String, Integer> lines = walkRepo(dir);
                        repositoryDataService.mergeLineCounts(lines);
                    }
                }));
    }

    public File cloneRepo(String repo_url, String repo_name) {
        String cloneDirectoryPath = "/tempRepos/" + repo_name + "/";
        try {
            logger.info("Cloning " + repo_url + " into " + cloneDirectoryPath);
            File dir = Paths.get(cloneDirectoryPath).toFile();
            if (dir.exists())
                FileUtils.deleteDirectory(dir);
            Git.cloneRepository()
                    .setURI(repo_url)
                    .setDirectory(dir)
                    .call();
            logger.info("Clone completed.");
            return dir;
        } catch (Exception e) {
            logger.info("Exception occurred while cloning repo " + repo_url);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param dir directory of file/dir
     * @return count of words of .java .js .cpp
     */
    private Map<String, Integer> walkRepo(File dir) {
        Map<String, Integer> languageCount = new HashMap<>();
        if (dir.isDirectory()) {
            File[] listFiles = dir.listFiles();
            for (File file : listFiles) {
                Map<String, Integer> res = walkRepo(file);
                res.forEach((key, value) -> languageCount.merge(key, value, (v1, v2) ->
                        v1 + v2));
            }
        } else if (suffixes.stream().anyMatch(p -> dir.getName().endsWith(p))) {
            logger.info("Scanning " + dir.getName() + "...");
            int count = 0;
            try (FileChannel channel = FileChannel.open(dir.toPath(), StandardOpenOption.READ)) {
                ByteBuffer byteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
                while (byteBuffer.hasRemaining()) {
                    byte currentByte = byteBuffer.get();
                    if (currentByte == '\n')
                        count++;
                }
                String[] parts = dir.getName().split("\\.");
                String suffix = parts[parts.length - 1];
                languageCount.put(suffix, languageCount.getOrDefault(suffix, 0) + count);
            } catch (IOException e) {
                logger.info("Error occurred while trying to read file " + dir.getName());
                e.printStackTrace();
            }
        }
        return languageCount;
    }
}
