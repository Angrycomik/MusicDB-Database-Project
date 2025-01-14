package bd;

public class Artist {
    private String artistName;
    private Integer artistID;
    private Integer startCareer;
    private Integer endCareer; 

    public Artist(String artistName, Integer startCareer,Integer endCareer) {
        this.artistName = artistName;
        this.startCareer = startCareer;
        this.endCareer = endCareer;
    }

    public String getName(){
        return artistName;
    }
    
}
