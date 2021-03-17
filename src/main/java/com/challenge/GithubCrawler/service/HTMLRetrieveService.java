package com.challenge.GithubCrawler.service;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class HTMLRetrieveService implements WebsiteRequestingService{

    @Autowired
    FileHandlerService fileHandler;

    public String getHTMLFromWebsite(String URL) throws IOException {
        HttpEntity entity = this.getRequestToURL(URL);

        return this.getHTMLTextFromBody(entity);
    }


    public byte[] getFileFromURL(String URL) throws IOException {
        HttpEntity entity = this.getRequestToURL(URL);

        return this.fileHandler.createFileByteArrayFromEntity(entity);
    }

    private HttpEntity getRequestToURL(String URL) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(URL);

        CloseableHttpResponse response = httpClient.execute(httpGet);

        return response.getEntity();
    }

    private String getHTMLTextFromBody(HttpEntity entity) throws IOException {
        return EntityUtils.toString(entity, "UTF-8");
    }
}
