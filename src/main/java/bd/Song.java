package bd;

public class Song {
    private String songName;
    private String artistName;
    private String albumName;
    private Integer songID;
    private Integer artistID;
    private Integer albumID;
    private Integer year;   

    public Song(String songName, String artistName, String albumName, Integer year) {
        this.songName = songName;
        this.artistName = artistName;
        this.albumName = albumName;
        this.year = year;
    }
    
    public String getName(){
        return songName;
    }
    public String getArtist(){
        return artistName;
    }
}