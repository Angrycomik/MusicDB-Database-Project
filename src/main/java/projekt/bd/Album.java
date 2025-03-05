package projekt.bd;

import javafx.scene.image.Image;

import java.util.ArrayList;

/**
 * This class represents album with main information about it and associated songs.
 */
public class Album {
    String albumName;
    String artistName;
    Integer albumID;
    Integer artistID;
    Integer releaseYear;
    Integer rating;
    ArrayList<String> songList;
    ArrayList<Integer> songIDList;
    Image image;

    /**
     * Constructor for the case when we want to have just the basic information.
     * @param albumName album name
     * @param artistName artist name
     * @param releaseYear release releaseYear
     * @param artistID artist id
     * @param albumID album id
     * @param image cover image
     */
    public Album(String albumName, String artistName, Integer releaseYear, Integer artistID, Integer albumID, Image image) {
        this.albumName = albumName;
        this.artistName = artistName;
        this.releaseYear = releaseYear;
        this.artistID = artistID;
        this.albumID = albumID;
        this.image = image;
    }

    /**
     * Constructor for the case when we want to have advanced information, such as song list.
     * @param albumName album name
     * @param artistName artist name
     * @param releaseYear release releaseYear
     * @param albumID album id
     * @param songList song list
     * @param songIDList list of song ids
     */
    public Album(String albumName, String artistName, Integer releaseYear, Integer albumID, Image cover, ArrayList<String> songList, ArrayList<Integer> songIDList) {
        this.albumName = albumName;
        this.artistName = artistName;
        this.releaseYear = releaseYear;
        this.albumID = albumID;
        this.image = cover;
        this.songList = songList;
        this.songIDList = songIDList;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getRating() {
        return rating;
    }

    public String getName() {
        return albumName;
    }

    public String getArtist() {
        return artistName;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public ArrayList<String> getSongList() {
        return songList;
    }

    public ArrayList<Integer> getSongIDList() {
        return songIDList;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public Integer getAlbumID() {
        return albumID;
    }

    public Integer getArtistID() {
        return artistID;
    }
}
