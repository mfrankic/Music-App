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

    private static ArrayList<Song> allSongs;

    private static int numOfFetchedURLs;


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



    public int getSongsCount(){return allSongs.size();}

    public static int getNumOfFetchedURLs() {
        return numOfFetchedURLs;
    }

    public static void setNumOfFetchedURLs(int numOfFetchedURLs) {
        DataSingleton.numOfFetchedURLs = numOfFetchedURLs;
    }
}

