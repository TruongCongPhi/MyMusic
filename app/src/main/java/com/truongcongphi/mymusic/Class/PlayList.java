package com.truongcongphi.mymusic.Class;

import java.io.Serializable;
import java.util.List;

public class PlayList implements Serializable {
    private String id, name, img;
    private List<String> listSongPlaylist;

    public String getId() {
        return id;
    }

    public PlayList(String id, String img, String name) {
        this.id = id;
        this.name = name;
        this.img = img;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<String> getListSongPlaylist() {
        return listSongPlaylist;
    }

    public void setListSongPlaylist(List<String> listSongPlaylist) {
        this.listSongPlaylist = listSongPlaylist;
    }
}
