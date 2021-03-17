package com.challenge.GithubCrawler.service;

import java.io.IOException;

public interface WebsiteRequestingService {
    String getHTMLFromWebsite(String URL) throws IOException;
    byte[] getFileFromURL(String URL) throws IOException;
    Boolean verifyIfFileIsTextFile(String URL) throws IOException;
}
