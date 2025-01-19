package projekt.bd;

import java.util.ArrayList;

public class Artist {
    private String artistName;
    private Integer artistID;
    private Integer startCareer;
    private Integer endCareer; 
    private Integer songCount;
    private ArrayList<String> songs;
    private ArrayList<Integer> songs_id;
    private ArrayList<String> albums;
    private ArrayList<Integer> albums_id;

    public Artist(String artistName, Integer startCareer,Integer endCareer,Integer songCount,ArrayList<String> songs,ArrayList<Integer> songs_id, ArrayList<String> albums,ArrayList<Integer> albums_id) {
        this.artistName = artistName;
        this.startCareer = startCareer;
        this.endCareer = endCareer;
        this.songCount = songCount;
        this.songs = songs;
        this.albums = albums;
        this.albums_id = albums_id;
        this.songs_id = songs_id;
    }
    public Artist(String artistName, Integer startCareer,Integer endCareer,Integer id) {
        this.artistName = artistName;
        this.startCareer = startCareer;
        this.endCareer = endCareer;
        this.artistID = id;
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
    public Integer getArtistID(){return artistID;}
    public ArrayList<Integer> getAlbumIDList(){return albums_id;}
    public ArrayList<Integer> getSongIDList(){return songs_id;}


}
