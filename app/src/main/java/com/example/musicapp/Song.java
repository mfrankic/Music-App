package com.example.musicapp;

import java.sql.Timestamp;

public class Song {

    private String songName, artistName, songFileUUID, genre, artistID, albumUUDI, albumName;
    private Timestamp releaseDate;

    public Song() {

    }

    public Timestamp getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Timestamp releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }









    public String getAlbumUUDI() {
        return albumUUDI;
    }

    public void setAlbumUUDI(String albumUUDI) {
        this.albumUUDI = albumUUDI;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getSongFileUUID() {
        return songFileUUID;
    }

    public void setSongFileUUID(String songFileUUID) {
        this.songFileUUID = songFileUUID;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getArtistID() {
        return artistID;
    }

    public void setArtistID(String artistID) {
        this.artistID = artistID;
    }

    @Override
    public String toString() {
        return "Song{" +
                "songName='" + songName + '\'' +
                ", artistName='" + artistName + '\'' +
                ", songFileUUID='" + songFileUUID + '\'' +
                ", genre='" + genre + '\'' +
                ", artistID='" + artistID + '\'' +
                ", albumUUDI='" + albumUUDI + '\'' +
  
                ", albumName='" + albumName + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
}
