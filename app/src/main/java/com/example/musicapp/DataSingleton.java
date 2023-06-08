package com.example.musicapp;

import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DataSingleton {

    private static DataSingleton instance;

    private static ArrayList<Song> allSongs, songsQueue;
    public static ArrayList<Song> playlistCreateSongs;
    private static ArrayList<String> allArtists, songPaths;
    private static ArrayList<Album> allAlbums;

    private static String currentUserName, currentUserID, currentUserBio;


    private DataSingleton(){

    }

    public static synchronized DataSingleton getDataSingleton(){
        if(instance == null){
            instance = new DataSingleton();

        }

        return instance;
    }

    public void setAllSongs(ArrayList<Song> allSongs){
        this.allSongs = allSongs;
    }

    public ArrayList<Song> getAllSongs(){
        return allSongs;
    }


    public  ArrayList<String> getAllArtists() {
        return allArtists;
    }

    public  void setAllArtists(ArrayList<String> allArtists) {
        DataSingleton.allArtists = allArtists;
    }

    public  ArrayList<Album> getAllAlbums() {
        return allAlbums;
    }

    public  void setAllAlbums(ArrayList<Album> allAlbums) {
        DataSingleton.allAlbums = allAlbums;
    }

    public  ArrayList<Song> getSongsQueue() {
        return songsQueue;
    }

    public  void setSongsQueue(ArrayList<Song> songsQueue) {
        DataSingleton.songsQueue = songsQueue;
    }


    public  ArrayList<String> getSongPaths() {
        return songPaths;
    }

    public  void setSongPaths(ArrayList<String> songPaths) {
        DataSingleton.songPaths = songPaths;
    }

    public int getSongsCount(){return allSongs.size();}


    public  String getCurrentUserName() {
        return currentUserName;
    }

    public  void setCurrentUserName(String currentUserName) {
        DataSingleton.currentUserName = currentUserName;
    }

    public  String getCurrentUserID() {
        return currentUserID;
    }

    public void setCurrentUserID(String currentUserID) {
        DataSingleton.currentUserID = currentUserID;
    }

    public  String getCurrentUserBio() {
        return currentUserBio;
    }

    public  void setCurrentUserBio(String currentUserBio) {
        DataSingleton.currentUserBio = currentUserBio;
    }

    public ArrayList<Song> getPlaylistCreateSongs() {
        return playlistCreateSongs;
    }

    public  void setPlaylistCreateSongs(ArrayList<Song> playlistCreateSongs) {
        DataSingleton.playlistCreateSongs = playlistCreateSongs;
    }

    public void addPlaylistCreateSongs(Song song){
        this.playlistCreateSongs.add(song);
    }
    public void removeFromPlaylistCreateSongs(Song removeSong){
        ArrayList<Song> tmpList = new ArrayList<>();
        for(Song song: playlistCreateSongs){
            if(!song.getSongFileUUID().equals(removeSong.getSongFileUUID())){
                tmpList.add(song);
            }
        }

        playlistCreateSongs = tmpList;
    }
}

