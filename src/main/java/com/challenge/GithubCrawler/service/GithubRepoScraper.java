package com.challenge.GithubCrawler.service;

import com.challenge.GithubCrawler.model.FileExtensionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Service
public class GithubRepoScraper implements GitRepositoryScraper{

    private static final String GITHUB_DOMAIN = "https://github.com";
    private static final String GITHUB_RAW_FILE_DOMAIN = "https://raw.githubusercontent.com";
    private static final String GITHUB_FILE_TYPE = "blob";
    private static final String GITHUB_TREE_REFERENCE = "tree";

    //Move this to Util/Constants
    private static final String URL_DEPTH_SEPARATOR = "/";

    private String projectName;
    private String branch;
    private List<String> visitedLinks;
    private Map<String, FileExtensionData> fileExtensionData;

    @Autowired
    private PageAndFileProcessingService pageAndFileProcessor;

    public Map<String, FileExtensionData> processRepository(String URL, String branch) throws IOException, ExecutionException, InterruptedException {
        this.initiateVariables(URL, branch);

        CompletableFuture<Void> isReady = this.pageAndFileProcessor.processPage(this.projectName);

        isReady.get();

        return fileExtensionData;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getBranch() {
        return branch;
    }

    public List<String> getVisitedLinks() {
        return visitedLinks;
    }

    public Map<String, FileExtensionData> getFileExtensionData() {
        return fileExtensionData;
    }

    public static String getGithubDomain() {
        return GITHUB_DOMAIN;
    }

    public static String getGithubRawFileDomain() {
        return GITHUB_RAW_FILE_DOMAIN;
    }

    public static String getGithubFileType() {
        return GITHUB_FILE_TYPE;
    }

    public static String getGithubTreeReference() {
        return GITHUB_TREE_REFERENCE;
    }

    public static String getUrlDepthSeparator() {
        return URL_DEPTH_SEPARATOR;
    }

    private void initiateVariables(String URL, String branch){
        this.projectName = this.getProjectNameFromURL(URL);
        this.branch = branch;
        this.visitedLinks = Collections.synchronizedList(new ArrayList<>());
        this.fileExtensionData = new ConcurrentHashMap<>();
    }

    private String getProjectNameFromURL(String URL) {
        return URL.substring(GITHUB_DOMAIN.length());
    }
}

