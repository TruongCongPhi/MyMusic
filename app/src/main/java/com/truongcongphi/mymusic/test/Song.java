package com.truongcongphi.mymusic.test;

public class Song {

        public String albumID;
        public String duration;
        public String singerName;
        public String songName;
        public String url;

        public Song() {
            // Default constructor required for calls to DataSnapshot.getValue(Song.class)
        }

        public Song(String albumID, String duration, String singerName, String songName, String url) {
            this.albumID = albumID;
            this.duration = duration;
            this.singerName = singerName;
            this.songName = songName;
            this.url = url;
        }

    public String getAlbumID() {
        return albumID;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
