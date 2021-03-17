package com.challenge.GithubCrawler.service;

import com.challenge.GithubCrawler.model.FileExtensionData;
import com.challenge.GithubCrawler.model.HTML;
import com.challenge.GithubCrawler.model.Link;
import com.challenge.GithubCrawler.model.TextFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class PageAndFileProcessorService implements PageAndFileProcessingService{
    @Autowired
    private WebsiteRequestingService websiteHTMLRetriever;

    @Autowired
    private HTMLParseService htmlParseService;

    @Autowired
    private FileHandlingService fileHandler;

    @Autowired
    private GithubRepoScraper githubRepoScraper;

    @Lazy
    @Autowired
    private LinkProcessingService LinkProcessor;

    @Async("asyncExecutor")
    public CompletableFuture<Void> processPage(String URL) throws IOException, ExecutionException, InterruptedException {

        HTML html = this.getHTMLFromURL(URL);

        CompletableFuture<Void> allLinksReady = this.sendAllLinksRequestProcess(html.getLinks());

        allLinksReady.get();

        return null;
    }

    @Async("asyncExecutor")
    public CompletableFuture<Void> processFileFromURL(Link link) throws IOException {

        String linkURL = this.generateRawFileURL(link);

        if(!this.websiteHTMLRetriever.verifyIfFileIsTextFile(linkURL))
            return null;

        byte[] textFile = this.websiteHTMLRetriever.getFileFromURL(linkURL);

        this.addVisitedFileLinesAndSize(
                new TextFile(
                        this.fileHandler.getFileExtensionFromURL(link.getHref()),
                        this.fileHandler.getTextFileLinesQty(textFile),
                        this.fileHandler.getFileLength(textFile)
                )
        );

        return null;
    }

    private HTML getHTMLFromURL(String URL) throws IOException {
        String htmlString = this.websiteHTMLRetriever.getHTMLFromWebsite(
                this.generateLinkPageURL(URL)
        );

        return new HTML(
                this.removeUnwantedLinksMainRepoPage(
                        this.htmlParseService.getAllLinksFromHTMLPage(htmlString)
                )
        );
    }

    private CompletableFuture<Void> sendAllLinksRequestProcess(Set<Link> links) throws InterruptedException, ExecutionException, IOException {
        ArrayList<CompletableFuture<Void>> linksReady = new ArrayList<>();

        for(Link link: links){
            linksReady.add(this.LinkProcessor.processLink(link));
        }

        return CompletableFuture.allOf(linksReady.toArray(new CompletableFuture[linksReady.size()]));
    }

    private Set<Link> removeUnwantedLinksMainRepoPage(Set<Link> links) {
        return links.stream().filter(this::checkIfRepoMainPageLink).collect(Collectors.toSet());
    }

    private Boolean checkIfRepoMainPageLink(Link link) {
        if (this.verifyRepoMainPageLinksRestrictions(link))
            return false;

        return link.getHref().substring(0, this.githubRepoScraper.getProjectName().length()).equals(this.githubRepoScraper.getProjectName());
    }

    private Boolean verifyRepoMainPageLinksRestrictions(Link link) {
        if (!link.getHref().contains(this.githubRepoScraper.getUrlDepthSeparator()))
            return true;

        if (link.getHref().length() < this.githubRepoScraper.getProjectName().length())
            return true;

        if (!link.getHref().contains(this.githubRepoScraper.getGithubFileType()) && !link.getHref().contains(this.githubRepoScraper.getGithubTreeReference()))
            return true;

        if (!link.getHref().contains(this.githubRepoScraper.getBranch()))
            return true;

        return false;
    }

    private String generateRawFileURL(Link link){
        String blobString = this.githubRepoScraper.getGithubRawFileDomain().concat(link.getHref());

        return blobString.replace("/".concat(this.githubRepoScraper.getGithubFileType()), "");
    }

    private String generateLinkPageURL(String pageURL){
        return this.githubRepoScraper.getGithubDomain().concat(pageURL);
    }

    public void addVisitedLink(String URL){
        this.githubRepoScraper.getVisitedLinks().add(URL);
    }

    private void addVisitedFileLinesAndSize(TextFile textFile){
        if(!this.githubRepoScraper.getFileExtensionData().containsKey(textFile.getFileExtension()))
            this.instantiateFileExtensionDataOnMap(textFile);

        this.updateFileExtensionDataValues(textFile);
    }

    private void instantiateFileExtensionDataOnMap(TextFile textFile){
        this.githubRepoScraper.getFileExtensionData().put(
                textFile.getFileExtension(),
                new FileExtensionData(
                        textFile.getFileExtension(),
                        0,
                        0
                )
        );
    }

    private void updateFileExtensionDataValues(TextFile textFile){
        FileExtensionData fileData = this.githubRepoScraper.getFileExtensionData().get(textFile.getFileExtension());

        fileData.setAccumulatedFileLinesQty(fileData.getAccumulatedFileLinesQty() + textFile.getLinesQty());
        fileData.setAccumulatedFileSize(fileData.getAccumulatedFileSize() + textFile.getSize());
    }
}
