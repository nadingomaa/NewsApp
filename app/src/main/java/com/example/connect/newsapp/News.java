package com.example.connect.newsapp;

public class News {

    private String title;
    private String sectionName;
    private String PublishedDate;
    private String webUrl;

    public News(String title, String sectionName, String publishedDate, String webUrl) {
        this.title = title;
        this.sectionName = sectionName;
        PublishedDate = publishedDate;
        this.webUrl = webUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getPublishedDate() {
        return PublishedDate;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
