package bd;

import java.util.ArrayList;


public class Album {
    String albumName;
    String artistName;
    Integer albumID;
    Integer artistID;
    Integer year;
    ArrayList<Song> songList = new ArrayList<>();
    byte[] cover;

    public Album(String albumName, String artistName, Integer year, byte[] cover) {
        this.albumName = albumName;
        this.artistName = artistName;
        this.year = year;
        this.cover = cover;
    }

    public String getName(){
        return albumName;
    }
    public String getArtist(){
        return artistName;
    }
}
