package com.challenge.GithubCrawler;

import com.challenge.GithubCrawler.service.MarkupLanguageParsingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MarkUpLanguageServiceTest {

    @Autowired
    private MarkupLanguageParsingService markupLanguageParsing;

    @Test
    public void testFirstTagOccurrence() {
        String html = "<body>\n".concat("<a href=\"/local\">Local</a>\n").concat("<a href=\"/main\">Main</a>\n").concat("</body>");

        String tag = this.markupLanguageParsing.getFirstTagOccurrence(html, "<a ", "/a>");

        assertEquals("<a href=\"/local\">Local</a>", tag);
    }

    @Test
    public void testFirstAttributeOccurrence() {
        String html = "<a href=\"/local\">Local</a>\n";

        String attr = this.markupLanguageParsing.getFirstAttributeOccurrence(html, "href");

        assertEquals("href=\"/local\"", attr);
    }

    @Test
    public void testAttributeValue(){
        String html = "href=\"/local\"";

        String value = this.markupLanguageParsing.getAttrValue(html);

        assertEquals("/local", value);
    }
}
