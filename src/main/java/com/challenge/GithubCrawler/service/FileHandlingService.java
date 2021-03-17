package com.challenge.GithubCrawler.service;

import org.apache.http.HttpEntity;

import java.io.IOException;

public interface FileHandlingService {
    byte[] createFileByteArrayFromEntity(HttpEntity entity) throws IOException;
    Integer getFileLength(byte[] file);
    Integer getTextFileLinesQty(byte[] file);
    String getFileExtensionFromURL(String URL);
}
