package projekt.bd;

import javafx.scene.image.Image;
/**
 * This class represents song with main information about it.
 */
public class Song {
    private final String songName;
    private final String artistName;
    private final String albumName;
    private Integer songID;
    private Integer artistID;
    private Integer songartistID;
    private final Integer releaseYear;
    private Image img;
    private Integer rating = null;

    /**
     * Constructor for a song class
     * @param songName name of song
     * @param song_id song id
     * @param artistName name of artist
     * @param artistID artist id
     * @param albumName name of album
     * @param releaseYear release year
     * @param img cover image
     */
    public Song(String songName, Integer song_id, String artistName, Integer artistID, String albumName, Integer releaseYear, Image img) {
        this.songName = songName;
        this.artistName = artistName;
        this.artistID = artistID;
        this.albumName = albumName;
        this.releaseYear = releaseYear;
        this.img = img;
        this.songID = song_id;
    }

    /**
     * Constructor for a song class with limited information.
     * @param songName name of song
     * @param artistName name of artist
     * @param albumName name of album
     * @param releaseYear release year
     * @param songartistid id of the song
     */
    public Song(String songName, String artistName, String albumName, Integer releaseYear, Integer songartistid) {
        this.songName = songName;
        this.artistName = artistName;
        this.albumName = albumName;
        this.releaseYear = releaseYear;
        this.songartistID = songartistid;
    }

    public void SetImage(Image image) {
        img = image;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Image getImage() {
        return img;
    }

    public String getName() {
        return songName;
    }

    public String getArtist() {
        return artistName;
    }

    public String getAlbum() {
        return albumName;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public Integer getSongArtistID() {
        return songartistID;
    }

    public Integer getRating() {
        return rating;
    }

    public Integer getSongID() {
        return songID;
    }

    public Integer getArtistID() {
        return artistID;
    }

}