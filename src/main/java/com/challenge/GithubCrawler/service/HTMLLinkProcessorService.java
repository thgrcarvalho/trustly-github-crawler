package com.challenge.GithubCrawler.service;

import com.challenge.GithubCrawler.model.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Service Class that is responsible for processing the links discovered by the API.
 *
 * @version 1.0
 *
 * @author Thiago Carvalho
 *
 */
@Service
public class HTMLLinkProcessorService implements LinkProcessingService {
    @Autowired
    private GithubRepoScraper githubRepoScraper;

    @Autowired
    private PageAndFileProcessingService pageAndFileProcessor;

    /**
     * Function responsible for processing a link found by the scraping process.
     *
     * @param link Requested link.
     * @return This function is responsible only for processing the URL. Nothing is returned.
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Async("asyncExecutor")
    public CompletableFuture<Void> processLink(Link link) throws IOException, ExecutionException, InterruptedException {

        if(this.verifyIfLinkedWasVisited(link.getHref()))
            return null;

        this.githubRepoScraper.getVisitedLinks().add(link.getHref());

        CompletableFuture<Void> isReady = this.sendPageOrFileRequestProcess(link);

        isReady.get();

        return null;
    }

    /**
     * Function responsible for sending the request to process all links found by the API process.
     *
     * @param link Requested link.
     * @return This function is responsible only for processing the URL. Nothing is returned.
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws IOException
     */
    private CompletableFuture<Void> sendPageOrFileRequestProcess(Link link) throws InterruptedException, ExecutionException, IOException {
        CompletableFuture<Void> isReady;

        if(this.checkIfLinkIsFile(link.getHref())){
            isReady = this.pageAndFileProcessor.processFileFromURL(link);
        } else {
            isReady = this.pageAndFileProcessor.processPage(link.getHref());
        }

        return isReady;
    }

    /**
     * Function that verifies if a link was already visited.
     *
     * @param URL Requested link URL.
     * @return The result of the verification.
     */
    private Boolean verifyIfLinkedWasVisited(String URL){
        return this.githubRepoScraper.getVisitedLinks().contains(URL);
    }

    /**
     * Function that verifies if a link is associated to a file, instead of a folder page.
     *
     * @param URL Requested link URL.
     * @return The result of the verification.
     */
    private Boolean checkIfLinkIsFile(String URL){
        return URL.contains(this.githubRepoScraper.getGithubFileType());
    }





}
