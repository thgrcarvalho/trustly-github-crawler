package com.challenge.GithubCrawler.service;

import com.challenge.GithubCrawler.model.Link;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface LinkProcessingService {
    CompletableFuture<Void> processLink(Link link) throws IOException, ExecutionException, InterruptedException;
}
