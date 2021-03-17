package com.challenge.GithubCrawler.controller;

import com.challenge.GithubCrawler.model.FileExtensionData;
import com.challenge.GithubCrawler.service.GitRepositoryScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/crawler")
public class WebsiteCrawlerController {

    @Autowired
    private GitRepositoryScraper githubRepoScraper;

    @GetMapping("/github-repository")
    public ResponseEntity<Map<String, FileExtensionData>> getWebsiteLinks(
            @RequestParam(value = "url") String URL,
            @RequestParam(value = "branch") String branch
            ) throws IOException, ExecutionException, InterruptedException {
        return ResponseEntity.ok(this.githubRepoScraper.processRepository(URL, branch));
    }
}
