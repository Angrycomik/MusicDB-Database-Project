package projekt.bd;

import java.util.ArrayList;

import javafx.scene.image.Image;


public class Album {
    String albumName;
    String artistName;
    Integer albumID;
    Integer artistID;
    Integer year;
    Integer rating;
    ArrayList<String> songList = new ArrayList<>();
    Image cover;

    public Album(String albumName, String artistName, Integer year) {
        this.albumName = albumName;
        this.artistName = artistName;
        this.year = year;
    }
    public Album(String albumName, String artistName, Integer year,Integer id, Image cover,ArrayList<String> songList) {
        this.albumName = albumName;
        this.artistName = artistName;
        this.year = year;
        this.albumID = id;
        this.cover = cover;
        this.songList = songList;
    }
    public void setRating(Integer rating){
        this.rating = rating;
    }
    public Integer getRating(){
        return rating;
    }
    public String getName(){
        return albumName;
    }
    public String getArtist(){
        return artistName;
    }
    public Integer getYear(){
        return year;
    }
    public ArrayList<String> getSongList(){
        return songList;
    }
    public Image getImage(){
        return cover;
    }
    public Integer getAlbumID(){
        return albumID;
    }
}
