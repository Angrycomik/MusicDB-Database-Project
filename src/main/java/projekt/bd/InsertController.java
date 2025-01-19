package projekt.bd;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;


public class InsertController {

    @FXML
    private Button addBtn;

    @FXML
    private TextField artistName;

    @FXML
    private Pane imagePane;

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
    HBox albumHBox;
    Label fileLabel;

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
        albumHBox = new HBox();
        rootVBox.getChildren().add(rootVBox.getChildren().size() - 1, albumHBox);
        albumHBox.setSpacing(225);

        albumTextField = new TextField();
        albumTextField.setPromptText("Nazwa płyty");
        albumHBox.getChildren().add(albumHBox.getChildren().size(), albumTextField);

        Button filePickerButton = new Button("Wybierz okładkę");
        albumHBox.getChildren().add(albumHBox.getChildren().size(), filePickerButton);

        fileLabel = new Label();
        rootVBox.getChildren().add(rootVBox.getChildren().size() - 1, fileLabel);

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        filePickerButton.setOnAction(event -> {
            file = fileChooser.showOpenDialog(filePickerButton.getScene().getWindow());
            if (file != null) {
                try {
                    fileLabel.setText(file.getPath());
                    BufferedImage img = Scalr.resize(ImageIO.read(file), 200, 150);
                    File resizedFile = new File("temp.jpg");
                    ImageIO.write(img, "jpg", resizedFile);
                    file = resizedFile;
                    Image image = new Image(new FileInputStream("temp.jpg"));
                    ImageView imageView = new ImageView(image);
                    imageView.setPreserveRatio(true);
                    imagePane.getChildren().clear();
                    imagePane.getChildren().add(imageView);
                    imageView.setLayoutX(182);
                    imageView.setLayoutY(50);
                } catch (Exception e) {
                    Utilities.showError(e);
                }
            }
        });
    }

    private void removeAlbumTextField() {
        if (albumTextField != null) {
            albumHBox.getChildren().clear();
            rootVBox.getChildren().remove(albumTextField);
            albumTextField = null;
            rootVBox.getChildren().remove(albumHBox);
            imagePane.getChildren().clear();
            file = null;
            rootVBox.getChildren().remove(fileLabel);
            fileLabel = null;
        }
    }


    @FXML
    void onAddClick(ActionEvent event) throws IOException {
        String tempAlbumName = null;
        if(albumTextField != null){
            tempAlbumName = albumTextField.getText();
        }
        Integer songYearInt = Utilities.parseInteger(year.getText());
        Integer albumYearInt = songYearInt;
        TempData.setData(songName.getText(),artistName.getText(),tempAlbumName,file,songYearInt,albumYearInt) ;
        
        try {
            if(!DatabaseManager.InDatabase(artistName.getText(),"artysta")){
                Utilities.showNotFound(artistName.getText());
                TempData.setYear(songYearInt);
                App.setRoot("insertartist");
            }else{
                if(DatabaseManager.insertSong(songName.getText(),artistName.getText(),songYearInt)){
                    if(tempAlbumName!=null){
                        DatabaseManager.insertAlbum(songName.getText(),artistName.getText(),DatabaseManager.getArtistID(artistName.getText()),tempAlbumName,albumYearInt,null);
                    }
                    App.setRoot("mainscene");
                }
                TempData.clear();
            } 
        } catch (Exception e) {
            Utilities.showError(e);
        }

    }

}
