package com.example.musicapp.entities;

import java.util.ArrayList;

public class DataSingleton {

    private static DataSingleton instance;

    private static ArrayList<Song> allSongs, songsQueue;
    private static ArrayList<String> allArtists, songPaths;
    private static ArrayList<Album> allAlbums;

    private static int numOfFetchedURLs;
    private static String currentUserName, currentUserID, currentUserBio;

    private DataSingleton() {

    }

    public static synchronized DataSingleton getDataSingleton() {
        if (instance == null) {
            instance = new DataSingleton();

        }

        return instance;
    }

    public void setAllSongs(ArrayList<Song> allSongs) {
        this.allSongs = allSongs;
    }

    public ArrayList<Song> getAllSongs() {
        return allSongs;
    }


    public ArrayList<String> getAllArtists() {
        return allArtists;
    }

    public void setAllArtists(ArrayList<String> allArtists) {
        DataSingleton.allArtists = allArtists;
    }

    public ArrayList<Album> getAllAlbums() {
        return allAlbums;
    }

    public void setAllAlbums(ArrayList<Album> allAlbums) {
        DataSingleton.allAlbums = allAlbums;
    }

    public ArrayList<Song> getSongsQueue() {
        return songsQueue;
    }

    public void setSongsQueue(ArrayList<Song> songsQueue) {
        DataSingleton.songsQueue = songsQueue;
    }


    public ArrayList<String> getSongPaths() {
        return songPaths;
    }

    public void setSongPaths(ArrayList<String> songPaths) {
        DataSingleton.songPaths = songPaths;
    }

    public int getSongsCount() {
        return allSongs.size();
    }


    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        DataSingleton.currentUserName = currentUserName;
    }

    public String getCurrentUserID() {
        return currentUserID;
    }

    public void setCurrentUserID(String currentUserID) {
        DataSingleton.currentUserID = currentUserID;
    }

    public String getCurrentUserBio() {
        return currentUserBio;
    }

    public void setCurrentUserBio(String currentUserBio) {
        DataSingleton.currentUserBio = currentUserBio;
    }
}

