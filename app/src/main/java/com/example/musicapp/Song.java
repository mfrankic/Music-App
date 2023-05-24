package com.example.musicapp;

public class Song {

    private String songName, artistName, songFileUUID, genre, artistID, albumUUDI;

    public Song() {

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
                ", artistUUID='" + artistID + '\'' +
                ", albumUUDI='" + albumUUDI + '\'' +
                '}';
    }
}
