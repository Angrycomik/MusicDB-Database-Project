package projekt.bd;

import org.controlsfx.control.Rating;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class SongPageController {

    @FXML
    private Label albumNameLabel;

    @FXML
    private Label artistNameLabel;

    @FXML
    private ImageView imageView;

    @FXML
    private Rating rating;

    @FXML
    private Label songNameLabel;

    @FXML
    private TextField year;
    
    private Song song;

    @FXML
    void setRating(MouseEvent event) {
        if(TempData.isUserLoggedIn()){
            DatabaseManager.addRating(song.getSongArtistID(), TempData.getUserID(), (int)rating.getRating());
            song.setRating((int)rating.getRating());
            changeRatingBehavior();
        }else{
            Utilities.showInformation("You need to log in in order to rate.");
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

    }

    public void initialize(){
        Integer temp = TempData.getChosen();
        try {
            song = DatabaseManager.getSongInfo(temp);

            songNameLabel.setText(song.getName());
            albumNameLabel.setText(song.getAlbum());
            Utilities.turnLabelClickable(albumNameLabel, temp, "albumpage", "d");

            artistNameLabel.setText(song.getArtist());
            Utilities.turnLabelClickable(artistNameLabel, temp, "artistpage", "d");

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
    }
}
