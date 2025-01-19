package projekt.bd;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import jdk.jshell.execution.Util;


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
    private TextField searchField;

    @FXML
    private ScrollPane songPane;
    @FXML
    void onKey(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)){
            getTextAndSearch();
        }
    }
    @FXML
    void onFocus(InputMethodEvent event) {
        getTextAndSearch();
    }

    void getTextAndSearch(){
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
        ArrayList<Artist> artists = DatabaseManager.searchArtists(query);
        ArrayList<Song> songs = DatabaseManager.searchSongs(query);
        ArrayList<Album> albums = DatabaseManager.searchAlbums(query);

        updateArtistResults(artists);
        updateSongResults(songs);
        updateAlbumResults(albums);
    }

    private void updateArtistResults(ArrayList<Artist> artists) {
        artistBox.getChildren().clear();
        for (Artist artist : artists) {
            Box box = new Box(artist.getName(),artist.getArtistID(),'v');
            artistBox.getChildren().add(box);
        }
        if(artists.isEmpty()){
            artistBox.getChildren().add(Utilities.notFoundLabel("No artists found"));
        }
    }

    private void updateSongResults(ArrayList<Song> songs) {
        songBox.getChildren().clear();
        for (Song song : songs) {
            Box box = new Box(song.getImage(), song.getName(), song.getArtist(),song.getSongID(),song.getArtistID(),'s');
            songBox.getChildren().add(box);
        }
        if(songs.isEmpty()){
            songBox.getChildren().add(Utilities.notFoundLabel("No songs found"));
        }
    }


    private void updateAlbumResults(ArrayList<Album> albums) {
        albumBox.getChildren().clear();
        for (Album album : albums) {
            Box box = new Box(album.getImage(), album.getName(), album.getArtist(),album.getAlbumID(),album.getArtistID(),'a');
            albumBox.getChildren().add(box);
        }
        if(albums.isEmpty()){
            albumBox.getChildren().add(Utilities.notFoundLabel("No albums found"));
        }
    }
}
