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

/**
 * Service Class that is responsible for processing the folders and files discovered by the API.
 *
 * @version 1.0
 *
 * @author Thiago Carvalho
 *
 */
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

    /**
     * Function responsible for processing a repository folder.
     *
     * @param URL Folder URL found by the scraping process.
     * @return This function is responsible only for processing the URL. Nothing is returned.
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Async("asyncExecutor")
    public CompletableFuture<Void> processPage(String URL) throws IOException, ExecutionException, InterruptedException {

        HTML html = this.getHTMLFromURL(URL);

        CompletableFuture<Void> allLinksReady = this.sendAllLinksRequestProcess(html.getLinks());

        allLinksReady.get();

        return null;
    }

    /**
     * Function responsible for processing a repository textFile.
     *
     * @param link File URL found by the scraping process.
     * @return This function is responsible only for processing the URL. Nothing is returned.
     * @throws IOException
     */
    @Async("asyncExecutor")
    public CompletableFuture<Void> processFileFromURL(Link link) throws IOException {

        String linkURL = this.generateRawFileURL(link);

        /**
         * Only text files should be analysed.
         */
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

    /**
     * Function responsible for generating an object with all useful links of a give folder page.
     *
     * @param URL The URL from the requested HTML website.
     * @return An Object with all the useful links for the API.
     * @throws IOException
     */
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

    /**
     * Function responsible for sending the request to process all links found in a page.
     *
     * @param links Page links that need to be processed.
     * @return This function is responsible only for processing the URL. Nothing is returned.
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws IOException
     */
    private CompletableFuture<Void> sendAllLinksRequestProcess(Set<Link> links) throws InterruptedException, ExecutionException, IOException {
        ArrayList<CompletableFuture<Void>> linksReady = new ArrayList<>();

        for(Link link: links){
            linksReady.add(this.LinkProcessor.processLink(link));
        }

        return CompletableFuture.allOf(linksReady.toArray(new CompletableFuture[linksReady.size()]));
    }

    /**
     * Function that removes all non useful links on a page.
     *
     * @param links List of links that need to be filtered.
     * @return Filtered list of links based on the API requirements.
     */
    private Set<Link> removeUnwantedLinksMainRepoPage(Set<Link> links) {
        return links.stream().filter(this::checkIfRepoMainPageLink).collect(Collectors.toSet());
    }

    /**
     * Verifies if a link belongs the to the requested repository.
     *
     * @param link Link to be verified.
     * @return The result of the verification.
     */
    private Boolean checkIfRepoMainPageLink(Link link) {
        if (this.verifyRepoMainPageLinksRestrictions(link))
            return false;

        return link.getHref().substring(0, this.githubRepoScraper.getProjectName().length()).equals(this.githubRepoScraper.getProjectName());
    }

    /**
     * Function that makes all the restrictions necessary to ensure that a folder page belongs to the requested
     * repository.
     *
     * @param link Folder page link to be verified.
     * @return The result of the verification.
     */
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

    /**
     * Function responsible for concatenating the repository name with a given link.
     *
     * @param link The link to be concatenated.
     * @return The full URL of the requested File.
     */
    private String generateRawFileURL(Link link){
        String blobString = this.githubRepoScraper.getGithubRawFileDomain().concat(link.getHref());

        return blobString.replace("/".concat(this.githubRepoScraper.getGithubFileType()), "");
    }

    /**
     * Function that generate the full URL of a given visited link.
     *
     * @param pageURL Page URL.
     * @return Full URL of the requested Page.
     */
    private String generateLinkPageURL(String pageURL){
        return this.githubRepoScraper.getGithubDomain().concat(pageURL);
    }

    /**
     * Function responsible for saving the data retrieved from the github text file.
     *
     * @param textFile An object that represents the text file.
     */
    private void addVisitedFileLinesAndSize(TextFile textFile){
        if(!this.githubRepoScraper.getFileExtensionData().containsKey(textFile.getFileExtension()))
            this.instantiateFileExtensionDataOnMap(textFile);

        this.updateFileExtensionDataValues(textFile);
    }

    /**
     * Function that instantiate the FileExtensionData object used on the HashMap.
     *
     * @param textFile Text file associated to the file extension.
     */
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

    /**
     * Function that update the data associated with the processed text file.
     *
     * @param textFile An object that represents the text file.
     */
    private void updateFileExtensionDataValues(TextFile textFile){
        FileExtensionData fileData = this.githubRepoScraper.getFileExtensionData().get(textFile.getFileExtension());

        fileData.setAccumulatedFileLinesQty(fileData.getAccumulatedFileLinesQty() + textFile.getLinesQty());
        fileData.setAccumulatedFileSize(fileData.getAccumulatedFileSize() + textFile.getSize());
    }
}
