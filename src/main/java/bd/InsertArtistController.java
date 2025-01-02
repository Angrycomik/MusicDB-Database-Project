package bd;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class InsertArtistController {

    @FXML
    private Button addBtn;

    @FXML
    private TextField artistName;

    @FXML
    private TextField endCareer;

    @FXML
    private VBox rootVBox;

    @FXML
    private TextField startCareer;

    @FXML
    public void initialize() {
        artistName.setText(TempData.getArtistName());

        startCareer.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                startCareer.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        endCareer.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                endCareer.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
    @FXML
    void onAddClick(ActionEvent event) throws IOException  {
        if(artistName.getText().trim().isEmpty() || startCareer.getText().trim().isEmpty()){
            return;
        }
        
        Integer startInt = Integer.parseInt(startCareer.getText());
        Integer endInt = null;
        if(!endCareer.getText().isEmpty()){
            endInt = Integer.parseInt(endCareer.getText());
        }
        
        if( Utilities.checkYear(startInt) && (endInt == null || Utilities.checkYear(endInt))){
            DatabaseManager.insertArtist(artistName.getText(), startInt, endInt);
            DatabaseManager.insertSong();
            TempData.clear();
            App.setRoot("mainscene");
        }
    }
}
