package bd;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainSceneController {

    @FXML
    private Button importButton;

    @FXML
    void onImportClick(ActionEvent event) throws IOException {
        App.setRoot("insertsong");
    }

}
