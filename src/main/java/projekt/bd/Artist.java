package projekt.bd;

import java.util.ArrayList;

/**
 * This class represents Artist with main information about it.
 */
public class Artist {
    private final String artistName;
    private Integer artistID;
    private Integer startCareer;
    private Integer endCareer;
    private Integer songCount;
    private ArrayList<String> songs;
    private ArrayList<Integer> songs_id;
    private ArrayList<String> albums;
    private ArrayList<Integer> albums_id;
    /**
     * Constructor for the case when we want to have advanced information, such as song list.
     * @param artistName artist name
     * @param startCareer year of the career beginning
     * @param endCareer year of the career end
     * @param songCount number of songs
     * @param songs song list
     * @param songs_id list of song ids
     * @param albums album list
     * @param albums_id list of album ids
     */
    public Artist(String artistName, Integer startCareer, Integer endCareer, Integer songCount, ArrayList<String> songs, ArrayList<Integer> songs_id, ArrayList<String> albums, ArrayList<Integer> albums_id) {
        this.artistName = artistName;
        this.startCareer = startCareer;
        this.endCareer = endCareer;
        this.songCount = songCount;
        this.songs = songs;
        this.albums = albums;
        this.albums_id = albums_id;
        this.songs_id = songs_id;
    }
    /**
     * Constructor for the case when we want to have basic information about the artist.
     * @param artistName artist name
     * @param startCareer year of the career beginning
     * @param endCareer year of the career end
     * @param id artists id
     */
    public Artist(String artistName, Integer startCareer, Integer endCareer, Integer id) {
        this.artistName = artistName;
        this.startCareer = startCareer;
        this.endCareer = endCareer;
        this.artistID = id;
    }
    /**
     * Constructor for the case when we want to have basic information about the artist.
     * @param artistName artist name
     * @param id artists id
     * @param songCount number of songs
     */
    public Artist(String artistName, Integer id,Integer songCount) {
        this.artistName = artistName;
        this.artistID = id;
        this.songCount = songCount;
    }


    public String getName() {
        return artistName;
    }

    public Integer getCareerStart() {
        return startCareer;
    }

    public Integer getCareerEnd() {
        return endCareer;
    }

    public ArrayList<String> getSongList() {
        return songs;
    }

    public ArrayList<String> getAlbumList() {
        return albums;
    }

    public Integer getArtistID() {
        return artistID;
    }

    public ArrayList<Integer> getAlbumIDList() {
        return albums_id;
    }

    public ArrayList<Integer> getSongIDList() {
        return songs_id;
    }
    public Integer getSongCount() {
        return songCount;
    }


}
