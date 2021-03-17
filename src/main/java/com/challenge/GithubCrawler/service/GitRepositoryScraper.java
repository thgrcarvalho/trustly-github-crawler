package com.challenge.GithubCrawler.service;

import com.challenge.GithubCrawler.model.FileExtensionData;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface GitRepositoryScraper {
    Map<String, FileExtensionData> processRepository(String URL, String branch) throws IOException, ExecutionException, InterruptedException;
}
