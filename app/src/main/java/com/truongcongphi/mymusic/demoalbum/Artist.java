package com.truongcongphi.mymusic.demoalbum;

public class Artist {
    private String imgURL,name;

    public Artist(String imgURL, String name) {
        this.imgURL = imgURL;
        this.name = name;
    }

    public Artist() {
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
