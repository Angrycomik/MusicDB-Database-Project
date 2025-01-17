package projekt.bd;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

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

    public void initialize(){
        Integer temp = TempData.getChosen();

        try {
            Artist artist = DatabaseManager.getArtistInfo(temp);

            artistNameLabel.setText(artist.getName());

            startTextField.setText(String.valueOf(artist.getCareerStart()));
            if(artist.getCareerEnd() == null || artist.getCareerEnd() == 0){
                centerVBox.getChildren().remove(tillLabel);
                centerVBox.getChildren().remove(endTextField);
            }else{
                endTextField.setText(String.valueOf(artist.getCareerEnd()));
            }
            updateSongList(artist.getSongList());
            updateAlbumList(artist.getAlbumList());
        } catch (Exception e) {
            Utilities.showError(e);
        }
    }
    private void updateSongList(ArrayList<String> songs) {
        songListBox.getChildren().clear();
        for (String songName : songs) {
            Label label = new Label(songName);
            songListBox.getChildren().add(label);
        }
    }
    private void updateAlbumList(ArrayList<String> albums) {
        albumListBox.getChildren().clear();
        for (String songName : albums) {
            Label label = new Label(songName);
            albumListBox.getChildren().add(label);
        }
    }
}
