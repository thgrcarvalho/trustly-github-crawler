package com.challenge.GithubCrawler.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class HTMLRetrieveService implements WebsiteRequestService{

    public String getHTMLFromWebsite(String URL) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = this.generateHttpRequest(URL);

        HttpResponse<String> response = this.getHTTPResponseFromClient(request, client);

        return this.getHTMLTextFromBody(response);
    }

    private HttpRequest generateHttpRequest(String URL){
        return HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .GET()
                .build();
    }

    private HttpResponse getHTTPResponseFromClient(HttpRequest request, HttpClient client) throws IOException, InterruptedException {
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private String getHTMLTextFromBody(HttpResponse<String> response){
        return response.body();
    }
}
