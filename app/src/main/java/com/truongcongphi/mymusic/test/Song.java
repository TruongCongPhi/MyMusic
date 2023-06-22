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
}
