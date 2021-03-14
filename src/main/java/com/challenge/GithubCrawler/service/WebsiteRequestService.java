package com.challenge.GithubCrawler.service;

import java.io.IOException;

public interface WebsiteRequestService {
    String getHTMLFromWebsite(String URL) throws IOException, InterruptedException;
}
