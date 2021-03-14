package com.challenge.GithubCrawler.controller;

import com.challenge.GithubCrawler.model.HTML;
import com.challenge.GithubCrawler.service.WebsiteHTMLRetrieverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequestMapping("/crawler")
public class WebsiteCrawlerController {

    @Autowired
    private WebsiteHTMLRetrieverService websiteHTMLRetriever;

    @GetMapping("/github-repository")
    public ResponseEntity<HTML> getMeasurement(@RequestParam(value = "url") String URL) throws IOException, InterruptedException {

        return ResponseEntity.ok(this.websiteHTMLRetriever.retrieveHTMLFromWebsite(URL));
    }

}
