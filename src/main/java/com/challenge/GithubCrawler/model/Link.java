package com.challenge.GithubCrawler.model;

public class Link {
    private String href;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Link(String href) {
        this.href = href;
    }

    @Override
    public String toString() {
        return "Link{" +
                "href='" + href + '\'' +
                '}';
    }
}
