package com.challenge.GithubCrawler.model;

import java.util.List;

public class HTML {
    private List<Link> links;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public HTML(List<Link> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "HTML{" +
                "links=" + links +
                '}';
    }
}
