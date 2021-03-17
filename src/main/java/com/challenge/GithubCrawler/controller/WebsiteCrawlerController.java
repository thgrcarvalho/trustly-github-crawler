package com.challenge.GithubCrawler.controller;

import com.challenge.GithubCrawler.model.HTML;
import com.challenge.GithubCrawler.service.HTMLParseService;
import com.challenge.GithubCrawler.service.HTMLRetrieveService;
import com.challenge.GithubCrawler.service.GithubRepoScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/crawler")
public class WebsiteCrawlerController {

    @Autowired
    private HTMLRetrieveService websiteHTMLRetriever;

    @Autowired
    private HTMLParseService htmlParseService;

    @Autowired
    private GithubRepoScraper githubRepoScraper;

    @GetMapping("/github-repository")
    public ResponseEntity<HTML> getWebsiteLinks(@RequestParam(value = "url") String URL) throws IOException {

        String htmlString = this.websiteHTMLRetriever.getHTMLFromWebsite(URL);

        HTML html = new HTML(this.htmlParseService.getAllLinksFromHTMLPage(htmlString));

        html.setLinks(this.githubRepoScraper.removeUnwantedLinksMainRepoPage(html.getLinks(), URL));

        html.getLinks().stream().map(link -> {
            if(link.getHref().contains("blob")){
                try {
                    this.githubRepoScraper.processFileFromURL(link);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }).collect(Collectors.toSet());

        return ResponseEntity.ok(html);
    }
}
