package com.example.newsapplication;

public class ANews {
    //variables
    private final String nHeading;
    private final String nTime;
    private String nSource;
    public String nThumbnail;

    //Constructor
    public ANews(String heading, String time, String source, String thumbnail){
        nHeading = heading;
        nTime = time;
        nSource = source;
        nThumbnail = thumbnail;
    }

    public String getHeading() {
        return nHeading;
    }

    public String getSource() {
        return nSource;
    }

    public String getTime() {
        return nTime;
    }

    public String getThumbnail() {
        return nThumbnail;
    }
}
