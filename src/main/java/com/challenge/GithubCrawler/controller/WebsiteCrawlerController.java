package com.challenge.GithubCrawler.controller;

import com.challenge.GithubCrawler.model.HTML;
import com.challenge.GithubCrawler.service.HTMLParseService;
import com.challenge.GithubCrawler.service.HTMLRetrieveService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private HTMLRetrieveService websiteHTMLRetriever;

    @Autowired
    private HTMLParseService htmlParseService;

    @GetMapping("/github-repository")
    public ResponseEntity<HTML> getMeasurement(@RequestParam(value = "url") String URL) throws IOException, InterruptedException {

        String htmlString = this.websiteHTMLRetriever.getHTMLFromWebsite(URL);

        return ResponseEntity.ok(new HTML(this.htmlParseService.getAllLinksFromHTMLPage(htmlString)));
    }

}
