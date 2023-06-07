package com.example.musicapp;

import java.sql.Timestamp;

public class Song {

    private String songName, artistName, songFileUUID, genre, artistID, albumUUDI, albumName, songPath, artistBio;
    private Timestamp releaseDate;
    private int numberOfLikes, numberOfListens;

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


    public String getSongPath() {
        return songPath;
    }

    public void setSongPath(String songPath) {
        this.songPath = songPath;
    }

    public String getArtistBio() {
        return artistBio;
    }

    public void setArtistBio(String artistBio) {
        this.artistBio = artistBio;
    }

    public int getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(String numberOfLikes) {
        this.numberOfLikes = Integer.valueOf(numberOfLikes);
    }

    public void setNumberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public int getNumberOfListens() {
        return numberOfListens;
    }

    public void setNumberOfListens(int numberOfListens) {
        this.numberOfListens = numberOfListens;
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
                ", songPath='" + songPath + '\'' +
                ", artistBio='" + artistBio + '\'' +
                ", releaseDate=" + releaseDate +
                '}';
    }
}
