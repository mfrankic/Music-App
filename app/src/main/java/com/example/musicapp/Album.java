package com.example.musicapp;


import java.sql.Timestamp;

public class Album {

    private String albumName, albumID;
    private Timestamp releaseDate;

    public Album() {
    }

    public String getAlbumID() {
        return albumID;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public Timestamp getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Timestamp releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "Album{" +
                "albumName='" + albumName + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", albumID='" + albumID + '\'' +
                '}';
    }
}
