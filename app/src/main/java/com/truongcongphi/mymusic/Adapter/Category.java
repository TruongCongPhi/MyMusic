package com.truongcongphi.mymusic.Adapter;

import com.truongcongphi.mymusic.Class.Album;

import java.util.List;

public class Category {
    private String nameCategory;
    private List<Album> albums;

    public Category(String nameCategory, List<Album> albums) {
        this.nameCategory = nameCategory;
        this.albums = albums;
    }

    public Category() {
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }
}
