package projekt.bd;

import java.util.ArrayList;

public class Artist {
    private String artistName;
    private Integer artistID;
    private Integer startCareer;
    private Integer endCareer; 
    private Integer songCount;
    private ArrayList<String> songs;
    private ArrayList<String> albums;

    public Artist(String artistName, Integer startCareer,Integer endCareer,Integer songCount,ArrayList<String> songs, ArrayList<String> albums) {
        this.artistName = artistName;
        this.startCareer = startCareer;
        this.endCareer = endCareer;
        this.songCount = songCount;
        this.songs = songs;
        this.albums = albums;
    }
    public Artist(String artistName, Integer startCareer,Integer endCareer) {
        this.artistName = artistName;
        this.startCareer = startCareer;
        this.endCareer = endCareer;
    }


    public String getName(){
        return artistName;
    }
    public Integer getCareerStart(){
        return startCareer;
    }
    public Integer getCareerEnd(){
        return endCareer;
    }
    public ArrayList<String> getSongList(){
        return songs;
    }
    public ArrayList<String> getAlbumList(){
        return albums;
    }
    
}
