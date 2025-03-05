package projekt.bd;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * This class is a controller for search.fxml file.
 */
public class SearchController {

    @FXML
    private VBox albumBox;

    @FXML
    private VBox artistBox;

    @FXML
    private VBox songBox;


    @FXML
    private ComboBox<Integer> comboBox;

    @FXML
    private TextField searchField1;

    @FXML
    private TextField searchField;

    /**
     * This method initializes ComboBox and adds regex listeners for some fields.
     */
    @FXML
    public void initialize() {
        comboBox.getItems().addAll(1, 2, 3, 4, 5);
        searchField1.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                searchField1.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
    /**
     * This method starts searching on ENTER key.
     * @param event on key pressed event
     */
    @FXML
    void onKey(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            getTextAndSearch();
        }
    }

    /**
     * This method starts searching on ENTER key.
     * @param event
     */
    @FXML
    void onKeyAvg(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            getTextAndSearchSongsRating();
        }
    }

    /**
     * This method calls function that searches artists that have N songs with rating above N. and populates VBox.
     */
    private void getTextAndSearchSongsRating() {
        String query = searchField1.getText().trim();
        if (query.isEmpty() || comboBox.getValue() == null) {
            clearSearchResults();
            return;
        }
        ArrayList<Artist> artists = DatabaseManager.getArtistsWithRatingsAbove(comboBox.getValue(),Utilities.parseInteger(query));
        artistBox.getChildren().clear();
        for (Artist artist : artists) {
            String str = artist.getName() + " has " + artist.getSongCount() + " songs with rating above " + comboBox.getValue();
            Box box = new Box(str, artist.getArtistID(), 'v');
            artistBox.getChildren().add(box);
        }
        if (artists.isEmpty()) {
            artistBox.getChildren().add(Utilities.notFoundLabel("No artists found"));
        }
    }

    /**
     * This method starts searching when search field is out of focus.
     * @param event changing input method event
     */
    @FXML
    void onFocus(InputMethodEvent event) {
        getTextAndSearch();
    }

    /**
     * This method gets the text and starts searching.
     */
    void getTextAndSearch() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            clearSearchResults();
            return;
        }
        performSearch(query);
    }

    /**
     * This method clears previous search results.
     */
    private void clearSearchResults() {
        artistBox.getChildren().clear();
        songBox.getChildren().clear();
        albumBox.getChildren().clear();
    }

    /**
     * This method performs search and calls methods to update three panes.
     * @param query
     */
    private void performSearch(String query) {
        ArrayList<Artist> artists = DatabaseManager.searchArtists(query);
        ArrayList<Song> songs = DatabaseManager.searchSongs(query);
        ArrayList<Album> albums = DatabaseManager.searchAlbums(query);

        updateArtistResults(artists);
        updateSongResults(songs);
        updateAlbumResults(albums);
    }

    /**
     * This method updates artist pane.
     * @param artists list of artists
     */
    private void updateArtistResults(ArrayList<Artist> artists) {
        artistBox.getChildren().clear();
        for (Artist artist : artists) {
            Box box = new Box(artist.getName(), artist.getArtistID(), 'v');
            artistBox.getChildren().add(box);
        }
        if (artists.isEmpty()) {
            artistBox.getChildren().add(Utilities.notFoundLabel("No artists found"));
        }
    }
    /**
     * This method updates songs pane.
     * @param songs list of songs
     */
    private void updateSongResults(ArrayList<Song> songs) {
        songBox.getChildren().clear();
        for (Song song : songs) {
            Box box = new Box(song.getImage(), song.getName(), song.getArtist(), song.getSongID(), song.getArtistID(), 's');
            songBox.getChildren().add(box);
        }
        if (songs.isEmpty()) {
            songBox.getChildren().add(Utilities.notFoundLabel("No songs found"));
        }
    }
    /**
     * This method updates albums pane.
     * @param albums list of artists
     */
    private void updateAlbumResults(ArrayList<Album> albums) {
        albumBox.getChildren().clear();
        for (Album album : albums) {
            Box box = new Box(album.getImage(), album.getName(), album.getArtist(), album.getAlbumID(), album.getArtistID(), 'a');
            albumBox.getChildren().add(box);
        }
        if (albums.isEmpty()) {
            albumBox.getChildren().add(Utilities.notFoundLabel("No albums found"));
        }
    }
}
