package com.challenge.GithubCrawler.service;

import com.challenge.GithubCrawler.model.HTML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class WebsiteHTMLRetrieverService {

    @Autowired
    private HTMLParseService hTMLParseService;

    public HTML retrieveHTMLFromWebsite(String URL) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return new HTML(this.hTMLParseService.getAllLinksFromHTMLPage(response.body()));
    }
}
