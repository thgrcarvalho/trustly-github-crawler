package com.challenge.GithubCrawler.service;

import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class ByteArrayHandlerService implements FileHandlingService{
    public byte[] createFileByteArrayFromEntity(HttpEntity entity) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        entity.writeTo(outputStream);

        return outputStream.toByteArray();
    }

    public Integer getFileLength(byte[] file){
        return file.length;
    }

    public Integer getTextFileLinesQty(byte[] file){
        String fileText = this.convertTextFileToString(file);

        return fileText.split("\\R").length;
    }

    public String getFileExtensionFromURL(String URL){
        return URL.contains(".") ? URL.substring(URL.lastIndexOf(".") + 1) : "";
    }

    private String convertTextFileToString(byte[] file){
        return new String(file, StandardCharsets.UTF_8);
    }
}
