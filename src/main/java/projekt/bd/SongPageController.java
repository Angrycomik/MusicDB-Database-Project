package projekt.bd;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import org.controlsfx.control.Rating;

import java.util.ArrayList;
/**
 * This class is a controller for songpage.fxml file.
 */
public class SongPageController {

    @FXML
    private Button addToPlaylistButton;

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
    private VBox reviewBox;

    @FXML
    private TextField reviewTextField;

    @FXML
    private Label songNameLabel;

    @FXML
    private TextField year;


    private Song song;
    Double globalRating;
    ArrayList<Review> reviews;
    /**
     * This method is used to set rating element's rating.
     * @param event On mouse click event.
     */
    @FXML
    void setRating(MouseEvent event) {
        if (TempData.isUserLoggedIn()) {
            DatabaseManager.addRating(song.getSongArtistID(), TempData.getUserID(), (int) rating.getRating(), 's');
            song.setRating((int) rating.getRating());
            changeRatingBehavior();
        } else {
            Utilities.showInformation("You need to log in in order to rate.");
        }
    }
    /**
     * This method is used to update the average rating.
     */
    private void globalRating() {
        globalRating = DatabaseManager.getAverageRating(song.getSongArtistID(), 's');
        System.out.println(globalRating);
        if (globalRating != null) {
            avgRating.setRating(globalRating);
        }
    }

    /**
     * This method resets the rating back to 0 after the user hovers and does not change the value.
     * @param event on mouse drag event
     */
    @FXML
    void resetRating(MouseEvent event) {
        rating.setRating(0);
        if (TempData.isUserLoggedIn() && song.getRating() != null) {
            rating.setRating(song.getRating());
        }
    }
    /**
     * This method updates some attributes of the rating element after using rates a song.
     */
    void changeRatingBehavior() {
        rating.setUpdateOnHover(false);
        rating.layout();
        rating.setRating(song.getRating());
        globalRating();

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
     * This method is initializing elements during the page loading, such as rating, label names and updates pane's content, also retrieves the information from the database.
     */
    public void initialize() {
        Integer temp = TempData.getChosen();
        try {
            song = DatabaseManager.getSongInfo(temp);

            songNameLabel.setText(song.getName());
            albumNameLabel.setText(song.getAlbum());
            Utilities.turnLabelClickable(albumNameLabel, temp, "albumpage", 'd');

            artistNameLabel.setText(song.getArtist());
            Utilities.turnLabelClickable(artistNameLabel, temp, "artistpage", 'd');

            year.setText(String.valueOf(song.getReleaseYear()));
            imageView.setImage(song.getImage());
        } catch (Exception e) {
            Utilities.showError(e);
        }
        if (TempData.isUserLoggedIn()) {
            song.setRating(DatabaseManager.getRating(TempData.getUserID(), song.getSongArtistID(), 's'));
            if (song.getRating() != null) {
                changeRatingBehavior();
            }
        }
        globalRating();
        updateReviewPane();
    }

    /**
     * This method opens a window for creating a playlist or adding song to a playlist.
     * @param event
     */
    @FXML
    void addToPlaylist(ActionEvent event) {
        if (!TempData.isUserLoggedIn()) {
            Utilities.showInformation("You need to log in in order to create playlist.");
            return;
        }
        Pair<ArrayList<String>, ArrayList<Integer>> pair = DatabaseManager.getUserPlaylists(TempData.getUserID());
        ArrayList<String> playlists = pair.getKey();
        ObservableList<String> comboBoxList = FXCollections.observableArrayList(playlists);
        ArrayList<Integer> playlistIDs = pair.getValue();

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Add to Playlist");
        dialog.setHeaderText("Choose a playlist or create a new one");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);

        VBox box = new VBox(10);
        box.setPrefWidth(300);

        ComboBox<String> playlistComboBox = new ComboBox<>(comboBoxList);
        playlistComboBox.setPromptText("Choose a playlist");

        TextField newPlaylistField = new TextField();
        newPlaylistField.setPromptText("Or create a new one");

        box.getChildren().addAll(new Label("Your playlists:"), playlistComboBox, new Label("New playlist:"), newPlaylistField);
        dialog.getDialogPane().setContent(box);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                if (!newPlaylistField.getText().isEmpty()) {
                    return newPlaylistField.getText();
                } else if (playlistComboBox.getValue() != null) {
                    return playlistComboBox.getValue();
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(selected -> {
            int index;
            if (!playlists.contains(selected)) {
                index = DatabaseManager.addPlaylist(selected);
                DatabaseManager.addSongToPlaylist(index, song.getSongArtistID());
            } else {
                index = playlists.indexOf(selected);
                DatabaseManager.addSongToPlaylist(playlistIDs.get(index), song.getSongArtistID());

            }


        });
    }

    /**
     * This method adds the review to a song.
     * @param event
     */
    @FXML
    void submitReview(ActionEvent event) {
        if (!TempData.isUserLoggedIn()) {
            Utilities.showInformation("You need to log in in order to submit a review");
            return;
        }
        String reviewText = reviewTextField.getText();
        if (DatabaseManager.addReview(reviewText, song.getSongArtistID())) {
            updateReviewPane();
        }
    }
    /**
     * This method retrieves reviews from database and updates the review pane.
     */
    private void updateReviewPane() {
        reviews = DatabaseManager.getReviews(song.getSongArtistID());
        reviewBox.getChildren().clear();
        for (int i = 0; i < reviews.size(); i++) {
            Review review = reviews.get(i);
            int userID = review.getUserID();
            String username = review.getUsername();
            String reviewText = review.getReviewText();
            Box box = new Box(username, userID, reviewText);
            reviewBox.getChildren().add(box);
        }
    }
}
