package bd;

public class Song {
    private String songName;
    private String artistName;
    private String albumName;
    private Integer songID;
    private Integer artistID;
    private Integer albumID;
    private Integer year;   

    public String getName(){
        return songName;
    }
    public String getArtist(){
        return artistName;
    }
}