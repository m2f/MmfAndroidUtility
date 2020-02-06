package com.makemefree.utility.example.model;

import android.text.SpannableString;

/**
 * A plain old Java object that holds a HTML string.
 */
public class NewsItem {

    private String text;
    private String html;
    private SpannableString[] arrayContent;

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

    public SpannableString[] getArrayContent() {
        return arrayContent;
    }

    public void setArrayContent(SpannableString[] arrayContent) {
        this.arrayContent = arrayContent;
    }
}

