package projekt.bd;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML
    private TextField loginTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerButton;

    @FXML
    void register(ActionEvent event) throws IOException {
        if(DatabaseManager.register(loginTextField.getText(), passwordField.getText())){
            Utilities.showInformation("Registered successfully!\nPlease log in.");
            App.setRoot("login");
        }
    }

}
