package com.challenge.GithubCrawler.service;

import com.challenge.GithubCrawler.model.Link;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface PageAndFileProcessingService {
    CompletableFuture<Void> processPage(String URL) throws IOException, ExecutionException, InterruptedException;
    CompletableFuture<Void> processFileFromURL(Link link) throws IOException;
}
