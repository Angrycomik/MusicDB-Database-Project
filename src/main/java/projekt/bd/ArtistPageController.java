package projekt.bd;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
/**
 * This class is a controller for artistpage.fxml file.
 */
public class ArtistPageController {

    @FXML
    private VBox albumListBox;

    @FXML
    private ScrollPane albumScrollPane;

    @FXML
    private Label artistNameLabel;

    @FXML
    private VBox centerVBox;

    @FXML
    private TextField endTextField;

    @FXML
    private ScrollPane singleScrollPane;

    @FXML
    private VBox songListBox;

    @FXML
    private TextField startTextField;

    @FXML
    private Label tillLabel;

    Artist artist;

    /**
     * This method is initializing elements during the page loading, such as label names and updates pane's content, also retrieves the information from the database.
     */
    public void initialize() {
        Integer temp = TempData.getChosen();

        try {
            artist = DatabaseManager.getArtistInfo(temp);

            artistNameLabel.setText(artist.getName());

            startTextField.setText(String.valueOf(artist.getCareerStart()));
            if (artist.getCareerEnd() == null || artist.getCareerEnd() == 0) {
                centerVBox.getChildren().remove(tillLabel);
                centerVBox.getChildren().remove(endTextField);
            } else {
                endTextField.setText(String.valueOf(artist.getCareerEnd()));
            }
            updateSongList(artist.getSongList());
            updateAlbumList(artist.getAlbumList());
        } catch (Exception e) {
            Utilities.showError(e);
        }
    }
    /**
     * This method updates the song pane from the given song list.
     * @param songs song list
     */
    private void updateSongList(ArrayList<String> songs) {
        songListBox.getChildren().clear();
        ArrayList<Integer> songsIDList = artist.getSongIDList();
        for (int i = 0; i < songs.size(); i++) {
            String songName = songs.get(i);
            Integer songID = songsIDList.get(i);
            Box box = new Box(songName, songID, 's');
            songListBox.getChildren().add(box);
        }
    }
    /**
     * This method updates the pane from the given albums.
     * @param albums album list
     */
    private void updateAlbumList(ArrayList<String> albums) {
        albumListBox.getChildren().clear();

        ArrayList<Integer> albumIDList = artist.getAlbumIDList();
        for (int i = 0; i < albums.size(); i++) {
            String albumName = albums.get(i);
            Integer albumID = albumIDList.get(i);
            System.out.println("album id for " + albumName + " is " + albumID);
            Box box = new Box(albumName, DatabaseManager.getSongFromAlbum(albumID), 'a');
            albumListBox.getChildren().add(box);
        }
    }
}
