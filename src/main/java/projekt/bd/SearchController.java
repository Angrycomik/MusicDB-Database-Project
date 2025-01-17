package projekt.bd;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;



public class SearchController {

    @FXML
    private VBox albumBox;

    @FXML
    private ScrollPane albumPane;

    @FXML
    private VBox artistBox;

    @FXML
    private ScrollPane artistPane;

    @FXML
    private VBox songBox;

    @FXML
    private ScrollPane songPane;
    @FXML
    void onKey(KeyEvent event) {
        TextField searchField = (TextField) event.getSource();
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            clearSearchResults();
            return;
        }
        performSearch(query);
    }

    private void clearSearchResults() {
        artistBox.getChildren().clear();
        songBox.getChildren().clear();
        albumBox.getChildren().clear();
    }

    private void performSearch(String query) {
        ArrayList<Artist> artists = searchArtists(query);
        ArrayList<Song> songs = searchSongs(query);
        ArrayList<Album> albums = searchAlbums(query);

        updateArtistResults(artists);
        updateSongResults(songs);
        updateAlbumResults(albums);
    }

    private void updateArtistResults(ArrayList<Artist> artists) {
        artistBox.getChildren().clear();
        for (Artist artist : artists) {
            Label artistLabel = new Label(artist.getName());
            artistBox.getChildren().add(artistLabel);
        }
    }

    private void updateSongResults(ArrayList<Song> songs) {
        songBox.getChildren().clear();
        for (Song song : songs) {
            Label songLabel = new Label(song.getName() + " - " + song.getArtist());
            songBox.getChildren().add(songLabel);
        }
    }

    private void updateAlbumResults(ArrayList<Album> albums) {
        albumBox.getChildren().clear();
        for (Album album : albums) {
            Label albumLabel = new Label(album.getName() + " by " + album.getArtist());
            albumBox.getChildren().add(albumLabel);
        }
    }

    private ArrayList<Artist> searchArtists(String query) {
        return DatabaseManager.searchArtists(query);
    }

    private ArrayList<Song> searchSongs(String query) {
        return DatabaseManager.searchSongs(query); 
    }

    private ArrayList<Album> searchAlbums(String query) {
        return DatabaseManager.searchAlbums(query);
    }

    
}
