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
    ArrayList<String> songList;
    ArrayList<Integer> songIDList;
    Image image;

    public Album(String albumName, String artistName, Integer year,Integer artistID,Integer albumID,Image image) {
        this.albumName = albumName;
        this.artistName = artistName;
        this.year = year;
        this.artistID = artistID;
        this.albumID = albumID;
        this.image = image;
    }
    public Album(String albumName, String artistName, Integer year,Integer albumID, Image cover,ArrayList<String> songList,ArrayList<Integer> songIDList) {
        this.albumName = albumName;
        this.artistName = artistName;
        this.year = year;
        this.albumID = albumID;
        this.image= cover;
        this.songList = songList;
        this.songIDList = songIDList;
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
    public ArrayList<Integer> getSongIDList(){return songIDList;}
    public void setImage(Image image){this.image= image;}
    public Image getImage(){
        return image;
    }
    public Integer getAlbumID(){
        return albumID;
    }
    public Integer getArtistID(){return artistID;}
}
