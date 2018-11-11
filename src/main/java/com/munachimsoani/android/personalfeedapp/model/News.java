package com.munachimsoani.android.personalfeedapp.model;

public class News {

    private String title;
    private String abstracts;
    private String url;
    private  String published_at;
    private String image_url;


    public News(){


    }

    public News(String title, String abstracts, String url, String published_at, String image_url) {
        this.title = title;
        this.abstracts = abstracts;
        this.url = url;
        this.published_at = published_at;
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublished_at() {
        return published_at;
    }

    public void setPublished_at(String published_at) {
        this.published_at = published_at;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
