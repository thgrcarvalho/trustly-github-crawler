package com.challenge.GithubCrawler.service;

import com.challenge.GithubCrawler.model.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class HTMLLinkProcessorService implements LinkProcessingService {
    @Autowired
    private GithubRepoScraper githubRepoScraper;

    @Autowired
    private PageAndFileProcessingService pageAndFileProcessor;

    @Async("asyncExecutor")
    public CompletableFuture<Void> processLink(Link link) throws IOException, ExecutionException, InterruptedException {

        if(this.verifyIfLinkedWasVisited(link.getHref()))
            return null;

        this.githubRepoScraper.getVisitedLinks().add(link.getHref());

        CompletableFuture<Void> isReady = this.sendPageOrFileRequestProcess(link);

        isReady.get();

        return null;
    }

    private CompletableFuture<Void> sendPageOrFileRequestProcess(Link link) throws InterruptedException, ExecutionException, IOException {
        CompletableFuture<Void> isReady;

        if(this.checkIfLinkIsFile(link.getHref())){
            isReady = this.pageAndFileProcessor.processFileFromURL(link);
        } else {
            isReady = this.pageAndFileProcessor.processPage(link.getHref());
        }

        return isReady;
    }

    private Boolean verifyIfLinkedWasVisited(String URL){
        return this.githubRepoScraper.getVisitedLinks().contains(URL);
    }

    private Boolean checkIfLinkIsFile(String URL){
        return URL.contains(this.githubRepoScraper.getGithubFileType());
    }





}
