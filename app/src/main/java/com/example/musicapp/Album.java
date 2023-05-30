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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Album other = (Album) obj;
        return this.getAlbumID() == other.getAlbumID() && this.getAlbumName().equals(other.getAlbumName());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + albumID.hashCode();
        result = 31 * result + albumName.hashCode();
        return result;
    }
}
