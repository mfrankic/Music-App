package com.example.musicapp.entities;


import java.sql.Timestamp;

public class Album {

    private String albumName, albumID, artistID;
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

    public String getArtistID() {
        return artistID;
    }

    public void setArtistID(String artistID) {
        this.artistID = artistID;
    }

    @Override
    public String toString() {
        return "Album{" +
                "albumName='" + albumName + '\'' +
                ", albumID='" + albumID + '\'' +
                ", artistID='" + artistID + '\'' +
                ", releaseDate=" + releaseDate +
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
