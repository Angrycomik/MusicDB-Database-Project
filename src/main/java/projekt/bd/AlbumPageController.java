package projekt.bd;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Rating;

import java.util.ArrayList;

/**
 * This class is a controller for albumpage.fxml file.
 */
public class AlbumPageController {

    @FXML
    private VBox albumListBox;

    @FXML
    private Label albumNameLabel;

    @FXML
    private Label artistNameLabel;

    @FXML
    private Rating avgRating;

    @FXML
    private ImageView imageView;

    @FXML
    private Rating rating;

    @FXML
    private ScrollPane songListScrollPane;

    @FXML
    private TextField year;

    Album album;
    Double globalRating;

    /**
     * This method is used to set rating element's rating.
     * @param event On mouse click event.
     */
    @FXML
    void setRating(MouseEvent event) {
        if (TempData.isUserLoggedIn()) {
            System.out.println(album.getAlbumID());
            DatabaseManager.addRating(album.getAlbumID(), TempData.getUserID(), (int) rating.getRating(), 'a');
            album.setRating((int) rating.getRating());
            changeRatingBehavior();
            globalRating();
        } else {
            Utilities.showInformation("You need to log in in order to rate.");
        }
    }

    /**
     * This method is used to update the average rating.
     */
    private void globalRating() {
        globalRating = DatabaseManager.getAverageRating(album.getAlbumID(), 'a');
        System.out.println(globalRating);
        if (globalRating != null) {
            avgRating.setRating(globalRating);
        }
    }

    /**
     * This method changes the average rating value back to its value if the user tries to change it.
     * @param event on mouse click event
     */
    @FXML
    void onAvgClick(MouseEvent event) {
        avgRating.setRating(globalRating);
    }

    /**
     * This method resets the average rating back to 0 after the user hovers and does not change the value.
     * @param event on mouse drag event
     */
    @FXML
    void resetRating(MouseEvent event) {
        rating.setRating(0);
        if (TempData.isUserLoggedIn() && album.getRating() != null) {
            rating.setRating(album.getRating());
        }
    }

    /**
     * This method updates some attributes of the rating element after using rates a song.
     */
    void changeRatingBehavior() {
        rating.setUpdateOnHover(false);
        rating.layout();
        rating.setRating(album.getRating());
        globalRating();
    }

    /**
     * This method is initializing elements during the page loading, such as rating, label names and updates pane's content, also retrieves the information from the database.
     */
    public void initialize() {
        Integer temp = TempData.getChosen();
        try {
            album = DatabaseManager.getAlbumInfo(temp);
            artistNameLabel.setText(album.getArtist());
            Utilities.turnLabelClickable(artistNameLabel, temp, "artistpage", 'd');
            albumNameLabel.setText(album.getName());
            year.setText(String.valueOf(album.getReleaseYear()));
            imageView.setImage(album.getImage());
            updateAlbumList(album.getSongList());
        } catch (Exception e) {
            Utilities.showError(e);
        }
        if (TempData.isUserLoggedIn()) {
            album.setRating(DatabaseManager.getRating(TempData.getUserID(), album.getAlbumID(), 'a'));
            if (album.getRating() != null) {
                changeRatingBehavior();
            }
        }
        globalRating();
    }

    /**
     * This method updates the pane from the given albums.
     * @param albums album list
     */
    private void updateAlbumList(ArrayList<String> albums) {
        albumListBox.getChildren().clear();
        ArrayList<Integer> songsIDList = album.getSongIDList();
        for (int i = 0; i < albums.size(); i++) {
            String albumName = albums.get(i);
            Integer songID = songsIDList.get(i);
            Box box = new Box(albumName, songID, 's');
            albumListBox.getChildren().add(box);
        }
    }
}
