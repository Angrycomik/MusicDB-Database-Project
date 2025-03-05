package projekt.bd;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

/**
 * This class is a controller for insertsong.fxml file.
 */
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
    /**
     * This method is initializing elements during the page loading, such as label names and updates pane's content, also retrieves the information from the database.
     */
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

    /**
     * This method adds fields, allowing user to add the album information.
     */
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
                    File tempFile = Files.createTempFile("temp", ".jpg").toFile();
                    ImageIO.write(img, "jpg", tempFile);
                    Image image = new Image(new FileInputStream(tempFile));
                    file =tempFile;

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

    /**
     * This method removes previously added fields for writing in album information.
     */
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

    /**
     * This method adds a song to the database.
     * @param event on mouse click event
     * @throws IOException
     */
    @FXML
    void onAddClick(ActionEvent event) throws IOException {
        String tempAlbumName = null;
        if (albumTextField != null && !albumTextField.getText().trim().equals("")) {
            tempAlbumName = albumTextField.getText();
        }
        Integer songYearInt = Utilities.parseInteger(year.getText());
        Integer albumYearInt = songYearInt;
        TempData.setData(songName.getText(), artistName.getText(), tempAlbumName, file, songYearInt);

        try {
            if (!songName.getText().trim().isEmpty() && songYearInt != null && !DatabaseManager.InDatabase(artistName.getText(), "artist")) {
                Utilities.showNotFound(artistName.getText());
                TempData.setYear(songYearInt);
                App.setRoot("insertartist");
            } else {
                if (DatabaseManager.insertSong(songName.getText(), artistName.getText(), songYearInt)) {
                    if (tempAlbumName != null) {
                        DatabaseManager.insertAlbum(songName.getText(), artistName.getText(), DatabaseManager.getArtistID(artistName.getText()), tempAlbumName, albumYearInt, null);
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
