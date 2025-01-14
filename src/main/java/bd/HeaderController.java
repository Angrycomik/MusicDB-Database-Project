package bd;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HeaderController {

    @FXML
    private Button logoBtn;

    @FXML
    void logoBtnClick(ActionEvent event)  throws IOException {
        App.setRoot("mainscene");
    }

    @FXML
    void onImportClick(ActionEvent event) throws IOException {
        App.setRoot("insertsong");
    }
    @FXML
    void onSearchClick(ActionEvent event) throws IOException {
        App.setRoot("search");
    }

}
