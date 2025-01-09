package bd;

import java.util.ArrayList;

import javafx.scene.image.Image;


public class Album {
    String albumName;
    String artistName;
    Integer albumID;
    Integer artistID;
    Integer year;
    ArrayList<Song> songList = new ArrayList<>();
    Image okladka;

    public String getName(){
        return albumName;
    }
    public String getArtist(){
        return artistName;
    }
}
