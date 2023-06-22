package com.truongcongphi.mymusic.demoalbum;

public class Album {
    private String albumName,singerName;
    private String albumURL;

    public Album( String albumURL,String albumName, String singerName) {
        this.albumURL = albumURL;
        this.albumName = albumName;
        this.singerName = singerName;

    }

    public Album() {
    }

    public String getAlbumURL() {
        return albumURL;
    }

    public void setAlbumURL(String albumURL) {
        this.albumURL = albumURL;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }
}
