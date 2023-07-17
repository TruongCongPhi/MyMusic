package com.truongcongphi.mymusic.Class;

import java.io.Serializable;
import java.util.List;

public class PlayList implements Serializable {
    private String playlist, other;
    private List<String> songLiked;

    public String getPlaylist() {
        return playlist;
    }

    public void setPlaylist(String playlist) {
        this.playlist = playlist;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public List<String> getSongLiked() {
        return songLiked;
    }

    public void setSongLiked(List<String> songLiked) {
        this.songLiked = songLiked;
    }
}
