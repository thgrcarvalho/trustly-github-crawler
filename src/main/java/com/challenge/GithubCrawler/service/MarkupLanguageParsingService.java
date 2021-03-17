package com.challenge.GithubCrawler.service;

public abstract class MarkupLanguageParsingService {
    private static final String ATTR_VALUE_START = "=\"";
    private static final String ATTR_VALUE_END="\"";


    public String getFirstTagOccurrence(String text, String tagStart, String tagEnd){
        String cleanText = this.removingLineBreaks(text);
        Integer tagEndSize = tagEnd.length();

        return cleanText.substring(
                cleanText.indexOf(tagStart),
                cleanText.indexOf(tagEnd) + tagEndSize
        );
    }

    public String getFirstAttributeOccurrence(String text, String attrName){
        String cleanText = this.removingLineBreaks(text);
        Integer indexOfAttrStart = cleanText.indexOf(attrName);

        if(!cleanText.contains(attrName)){
            return attrName.concat(ATTR_VALUE_START).concat(ATTR_VALUE_END);
        }

        return cleanText.substring(
                indexOfAttrStart,
                cleanText.indexOf(ATTR_VALUE_END, this.calculateFirstIndexAfterAttrStart(cleanText, attrName)) + 1
        );
    }

    public String getAttrValue(String attribute){
        String cleanText = this.removingLineBreaks(attribute);
        Integer indexOfAttrValueStart = cleanText.indexOf(ATTR_VALUE_START) + ATTR_VALUE_START.length();

        return attribute.substring(
                indexOfAttrValueStart,
                cleanText.indexOf(ATTR_VALUE_END, indexOfAttrValueStart)
        );
    }

    private String removingLineBreaks(String text){
        return text.replaceAll("\\R+", " ");
    }

    private Integer calculateFirstIndexAfterAttrStart(String text, String attrName){
        String attrNameWithValueDelimiter = attrName.concat(ATTR_VALUE_START);

        return text.indexOf(attrNameWithValueDelimiter) + attrNameWithValueDelimiter.length();
    }
}
