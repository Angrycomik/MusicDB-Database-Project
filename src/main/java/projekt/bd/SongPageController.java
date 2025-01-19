package projekt.bd;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import org.controlsfx.control.Rating;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

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

    @FXML
    void setRating(MouseEvent event) {
        if(TempData.isUserLoggedIn()){
            DatabaseManager.addRating(song.getSongArtistID(), TempData.getUserID(), (int)rating.getRating(),'s');
            song.setRating((int)rating.getRating());
            changeRatingBehavior();
        }else{
            Utilities.showInformation("You need to log in in order to rate.");
        }
    }
    private void globalRating(){
        globalRating = DatabaseManager.getAverageRating(song.getSongArtistID(),'s');
        System.out.println(globalRating);
        if(globalRating != null){
            avgRating.setRating(globalRating);
        }
    }

    @FXML
    void resetRating(MouseEvent event) {
        rating.setRating(0);
        if(TempData.isUserLoggedIn() && song.getRating()!=null){
            rating.setRating(song.getRating());
        }
    }
    void changeRatingBehavior(){
        rating.setUpdateOnHover(false);
        rating.layout();
        rating.setRating(song.getRating());
        globalRating();

    }

    @FXML
    void onAvgClick(MouseEvent event) {
            avgRating.setRating(globalRating);
    }

    public void initialize(){
        Integer temp = TempData.getChosen();
        try {
            song = DatabaseManager.getSongInfo(temp);

            songNameLabel.setText(song.getName());
            albumNameLabel.setText(song.getAlbum());
            Utilities.turnLabelClickable(albumNameLabel, temp, "albumpage", 'd');

            artistNameLabel.setText(song.getArtist());
            Utilities.turnLabelClickable(artistNameLabel, temp, "artistpage", 'd');

            year.setText(String.valueOf(song.getYear()));
            imageView.setImage(song.getImage());
        } catch (Exception e) {
            Utilities.showError(e);
        }
        if(TempData.isUserLoggedIn()){
            song.setRating(DatabaseManager.getRating(TempData.getUserID(),song.getSongArtistID(),'s'));
            if(song.getRating()!=null){
                changeRatingBehavior();
            }
        }
        globalRating();
        updateReviewPane();
    }

    @FXML
    void addToPlaylist(ActionEvent event) {
        if(!TempData.isUserLoggedIn()){
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
                DatabaseManager.addSongToPlaylist(index,song.getSongArtistID());
            }else{
                index = playlists.indexOf(selected);
                DatabaseManager.addSongToPlaylist(playlistIDs.get(index),song.getSongArtistID());

            }


        });
    }

    @FXML
    void submitReview(ActionEvent event) {
        if(!TempData.isUserLoggedIn()){
            Utilities.showInformation("You need to log in in order to submit a review");
            return;
        }
        String reviewText = reviewTextField.getText();
        if(DatabaseManager.addReview(reviewText)){
            updateReviewPane();
        }
    }
    private void updateReviewPane() {
        reviews = DatabaseManager.getReviews();
        reviewBox.getChildren().clear();
        for (int i = 0; i < reviews.size(); i++) {
            Review review = reviews.get(i);
            int userID = review.getUserID();
            String username = review.getUsername();
            String reviewText = review.getReviewText();
            Box box = new Box(username,userID,reviewText);
            reviewBox.getChildren().add(box);
        }
    }
}
