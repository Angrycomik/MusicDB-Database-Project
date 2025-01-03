package bd;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class InsertController {

    @FXML
    private Button addBtn;

    @FXML
    private TextField artistName;

    @FXML
    private RadioButton albumRadio;

    @FXML
    private RadioButton singleRadio;

    @FXML
    private VBox rootVBox;

    @FXML
    private TextField songName;
    
    @FXML
    private ToggleGroup songType;

    @FXML
    private TextField year;

    @FXML
    private TextField albumTextField;

    File file = null;

    @FXML
    public void initialize() {
        
        year.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                year.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        songType.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == albumRadio) {
                addAlbumTextField();
            } else if (newValue == singleRadio) {
                removeAlbumTextField();
            }
        });
    }

    private void addAlbumTextField() {
        if (albumTextField == null) {

            HBox albumHBox = new HBox();
            rootVBox.getChildren().add(rootVBox.getChildren().size() - 1, albumHBox);
            albumHBox.setSpacing(225);

            albumTextField = new TextField();
            albumTextField.setPromptText("Nazwa płyty");
            albumHBox.getChildren().add(albumHBox.getChildren().size(), albumTextField);

            Button filePickerButton = new Button("Wybierz okładkę");
            albumHBox.getChildren().add(albumHBox.getChildren().size(), filePickerButton);

            Label fileLabel = new Label();
            rootVBox.getChildren().add(rootVBox.getChildren().size() - 1, fileLabel);

            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

            filePickerButton.setOnAction(event -> {
                file = fileChooser.showOpenDialog(filePickerButton.getScene().getWindow());
                if (file != null) {
                    fileLabel.setText(file.getPath());
                }
            });
        }
        
    }

    private void removeAlbumTextField() {
        if (albumTextField != null) {
            rootVBox.getChildren().remove(albumTextField);
            albumTextField = null;
        }
    }


    @FXML
    void onAddClick(ActionEvent event) throws IOException {
        if(artistName.getText().trim().isEmpty() || songName.getText().trim().isEmpty() || 
                             (albumRadio.isSelected() && albumTextField.getText().trim().isEmpty())){
            return;
        }
        String tempAlbumName = null;
        if(albumTextField != null){
            tempAlbumName = albumTextField.getText();
        }
        Integer songYearInt = Integer.valueOf(year.getText());
        // Integer albumYearInt = Integer.valueOf(albumTextField.getText());
        Integer albumYearInt = songYearInt; // Implement
        TempData.setData(songName.getText(),artistName.getText(),tempAlbumName,file,songYearInt,albumYearInt) ;
        if( Utilities.checkYear(songYearInt)){
            try {
                if(!DatabaseManager.InDatabase(artistName.getText(),"artysta")){
                    Utilities.showNotFound(artistName.getText());
                    App.setRoot("insertartist");
                }else{
                    DatabaseManager.insertSong();
                    TempData.clear();
                    App.setRoot("mainscene");
                } 
            } catch (Exception e) {
                Utilities.showError(e);
            }

        }
    }

}
