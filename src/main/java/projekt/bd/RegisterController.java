package projekt.bd;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
/**
 * This class is a controller for register.fxml file.
 */
public class RegisterController {

    @FXML
    private TextField loginTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerButton;

    /**
     * This method is used to register user.
     * @param event mouse click event
     * @throws IOException
     */
    @FXML
    void register(ActionEvent event) throws IOException {
        if (DatabaseManager.register(loginTextField.getText(), passwordField.getText())) {
            Utilities.showInformation("Registered successfully!\nPlease log in.");
            App.setRoot("login");
        }
    }

}
