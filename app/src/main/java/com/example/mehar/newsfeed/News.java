package com.example.mehar.newsfeed;

public class News {
    private String mSectionName;
    private String mPublishDateAndTime;
    private String mWebURL;
    private String mWebTitle;
    private String mContributor;

    public News(String sectionName, String publishDateAndTime, String webURL, String webTitle, String contributor) {
        mPublishDateAndTime = publishDateAndTime;
        mSectionName = sectionName;
        mWebTitle = webTitle;
        mWebURL = webURL;
        mContributor = contributor;
    }

    public String getPublishDateAndTime() {
        return mPublishDateAndTime;
    }

    public String getWebTitle() {
        return mWebTitle;
    }

    public String getWebURL() {
        return mWebURL;
    }

    public String getSectionName() {
        return mSectionName;
    }

    public String getContributor() {
        return mContributor;
    }
}
