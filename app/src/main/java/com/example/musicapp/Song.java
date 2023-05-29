package com.example.musicapp;

public class Song {
    private int songId;
    private String title;
    private String artist;
    private String album;
    private String songPath;

    public Song(int songId, String title, String artist, String album, String songPath) {
        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.songPath = songPath;
    }

    public int getSongId() {
        return songId;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getSongPath() {
        return songPath;
    }
}
