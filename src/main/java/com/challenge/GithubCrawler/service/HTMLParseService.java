package com.challenge.GithubCrawler.service;

import com.challenge.GithubCrawler.model.Link;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HTMLParseService extends MarkupLanguageParsingService{
    private static final String BODY_START = "<body";
    private static final String BODY_END = "/body>";
    private static final String LINK_START = "<a ";
    private static final String LINK_END = "/a>";
    private static final String HREF_NAME = "href";

    public List<Link> getAllLinksFromHTMLPage(String html){
        String htmlBody = this.getHTMLBody(html);
        ArrayList<Link> links = new ArrayList<>();

        while(this.checkIfHTMLBodyHasLink(htmlBody)){
            htmlBody = processLinkFromHTMLBody(htmlBody, links);
        }

        return links;
    }

    private String getHTMLBody(String html){
        return super.getFirstTagOccurrence(html, BODY_START, BODY_END);
    }

    private String getFirstLinkFromHTML(String html){
        return super.getFirstTagOccurrence(html, LINK_START, LINK_END);
    }

    private String removeFirstLinkFromHTML(String link, String html){
        return html.replace(link, "");
    }

    private Boolean checkIfHTMLBodyHasLink(String html){
        return html.contains(LINK_START);
    }

    private String getHrefFromLink(String link){
        return super.getFirstAttributeOccurrence(link, HREF_NAME);
    }

    private String processLinkFromHTMLBody(String htmlBody, List<Link> links){
        String htmlLink = this.getFirstLinkFromHTML(htmlBody);

        Link link = this.generateLinkFromHTML(htmlLink);

        links.add(link);

        return this.removeFirstLinkFromHTML(htmlLink, htmlBody);
    }

    private Link generateLinkFromHTML(String htmlLink){
        String href = this.getHrefFromLink(htmlLink);

        Link link = new Link(super.getAttrValue(href));

        return link;
    }
}
