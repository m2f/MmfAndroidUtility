package com.makemefree.utility.example.model;

/**
 * A plain old Java object that holds a HTML string.
 */
public class NewsItem {

    private String text;
    private String html;
    private String[] arrayContent;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String[] getArrayContent() {
        return arrayContent;
    }

    public void setArrayContent(String[] arrayContent) {
        this.arrayContent = arrayContent;
    }
}

