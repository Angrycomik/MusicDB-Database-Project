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

    @FXML
    void setRating(MouseEvent event) {
        if(TempData.isUserLoggedIn()){
            System.out.println(album.getAlbumID());
            DatabaseManager.addRating(album.getAlbumID(), TempData.getUserID(), (int)rating.getRating(),'a');
            album.setRating((int)rating.getRating());
            changeRatingBehavior();
            globalRating();
        }else{
            Utilities.showInformation("You need to log in in order to rate.");
        }
    }
    private void globalRating(){
        globalRating = DatabaseManager.getAverageRating(album.getAlbumID(),'a');
        System.out.println(globalRating);
        if(globalRating != null){
            avgRating.setRating(globalRating);
        }
    }

    @FXML
    void onAvgClick(MouseEvent event) {
        avgRating.setRating(globalRating);
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
        globalRating();
    }

    public void initialize(){
        Integer temp = TempData.getChosen();
        try {
            album = DatabaseManager.getAlbumInfo(temp);
            artistNameLabel.setText(album.getArtist());
            Utilities.turnLabelClickable(artistNameLabel, temp, "artistpage", 'd');

            albumNameLabel.setText(album.getName());
            year.setText(String.valueOf(album.getYear()));
            imageView.setImage(album.getImage());
            updateAlbumList(album.getSongList());
        } catch (Exception e) {
            Utilities.showError(e);
        }
        if(TempData.isUserLoggedIn()){
            album.setRating(DatabaseManager.getRating(TempData.getUserID(),album.getAlbumID(),'a'));
            if(album.getRating()!=null){
                changeRatingBehavior();
            }
        }
        globalRating();
    }

    private void updateAlbumList(ArrayList<String> albums) {
        albumListBox.getChildren().clear();
        ArrayList<Integer> songsIDList = album.getSongIDList();
        for (int i = 0; i < albums.size(); i++) {
            String albumName = albums.get(i);
            Integer songID = songsIDList.get(i);
            Box box = new Box(albumName, songID,'s');
            albumListBox.getChildren().add(box);
        }
    }
}
