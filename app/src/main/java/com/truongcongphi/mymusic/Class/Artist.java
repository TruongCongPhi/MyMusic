package com.truongcongphi.mymusic.Class;

import java.io.Serializable;
import java.util.List;

public class Artist implements Serializable {
    private String imgURL,name;
    private List<String> listSongArtist;

    public List<String> getListSongArtist() {
        return listSongArtist;
    }

    public void setListSongArtist(List<String> listSongArtist) {
        this.listSongArtist = listSongArtist;
    }

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
