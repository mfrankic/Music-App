package com.example.musicapp.entities;

import java.util.ArrayList;

public class User {

    private String userName, userBio, userID;
    private ArrayList<ArrayList<Song>> playlists;
    private boolean isArtist;

    public User() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isArtist() {
        return isArtist;
    }

    public void setArtist(boolean artist) {
        isArtist = artist;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

    public ArrayList<ArrayList<Song>> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(ArrayList<ArrayList<Song>> playlists) {
        this.playlists = playlists;
    }
}
