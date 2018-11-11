package com.munachimsoani.android.personalfeedapp.model;

public class MovieReview {

    private String displayTitle;
    private String byLine;
    private String headline;
    private String summaryShort;
    private String publicationDate;
    private String url;
    private String imageUrl;

    public MovieReview(String displayTitle, String byLine, String headline, String summaryShort, String publicationDate, String url, String imageUrl) {
        this.displayTitle = displayTitle;
        this.byLine = byLine;
        this.headline = headline;
        this.summaryShort = summaryShort;
        this.publicationDate = publicationDate;
        this.url = url;
        this.imageUrl = imageUrl;
    }

    public String getDisplayTitle() {
        return displayTitle;
    }

    public void setDisplayTitle(String displayTitle) {
        this.displayTitle = displayTitle;
    }

    public String getByLine() {
        return byLine;
    }

    public void setByLine(String byLine) {
        this.byLine = byLine;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getSummaryShort() {
        return summaryShort;
    }

    public void setSummaryShort(String summaryShort) {
        this.summaryShort = summaryShort;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
