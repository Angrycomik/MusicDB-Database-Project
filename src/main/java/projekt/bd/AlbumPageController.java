package projekt.bd;

import java.util.ArrayList;

import org.controlsfx.control.Rating;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class AlbumPageController {

    @FXML
    private Label artistNameLabel;

    @FXML
    private ImageView imageView;

    @FXML
    private VBox albumListBox;

    @FXML
    private ScrollPane albumListScrollPane;

    @FXML
    private Label albumNameLabel;

    @FXML
    private Rating rating;

    @FXML
    private TextField year;
    Album album;

    @FXML
    void setRating(MouseEvent event) {
        if(TempData.isUserLoggedIn()){
            DatabaseManager.addRating(album.getAlbumID(), TempData.getUserID(), (int)rating.getRating());
            album.setRating((int)rating.getRating());
            changeRatingBehavior();
        }else{
            Utilities.showInformation("You need to log in in order to rate.");
        }
    }

    @FXML
    void resetRating(MouseEvent event) {
        rating.setRating(0);
        if(TempData.isUserLoggedIn() && album.getRating()!=null){
            rating.setRating(album.getRating());
        }
    }
    void changeRatingBehavior(){
        rating.setUpdateOnHover(false);
        rating.layout();
        rating.setRating(album.getRating());
    }

    public void initialize(){
        Integer temp = TempData.getChosen();
        try {
            album = DatabaseManager.getAlbumInfo(temp);
            artistNameLabel.setText(album.getArtist());
            Utilities.turnLabelClickable(artistNameLabel, temp, "artistpage", "d");

            albumNameLabel.setText(album.getName());
            year.setText(String.valueOf(album.getYear()));
            imageView.setImage(album.getImage());
            updatealbumList(album.getSongList());
        } catch (Exception e) {
            Utilities.showError(e);
        }
        if(TempData.isUserLoggedIn()){
            album.setRating(DatabaseManager.getRating(TempData.getUserID(),album.getAlbumID(),'a'));
            if(album.getRating()!=null){
                changeRatingBehavior();
            }
        }
    }

    private void updatealbumList(ArrayList<String> albums) {
        albumListBox.getChildren().clear();
        for (String albumName : albums) {
            Label artistLabel = new Label(albumName);
            albumListBox.getChildren().add(artistLabel);
        }
    }
}
