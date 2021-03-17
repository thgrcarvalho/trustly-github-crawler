package com.challenge.GithubCrawler.model;

import java.util.Set;

public class HTML {
    private Set<Link> links;

    public HTML(Set<Link> links) {
        this.links = links;
    }

    public Set<Link> getLinks() {
        return links;
    }

    public void setLinks(Set<Link> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "HTML{" +
                "links=" + links +
                '}';
    }
}
