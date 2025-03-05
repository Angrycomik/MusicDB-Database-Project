package projekt.bd;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
/**
 * This class is a controller for login.fxml file.
 */
public class LoginController {

    @FXML
    private Button loginButton;

    @FXML
    private TextField loginTextField;

    @FXML
    private PasswordField passwordField;
    /**
     * This method logs user in.
     * @param event on mouse click event
     * @throws IOException
     */
    @FXML
    void login(ActionEvent event) throws IOException {
        String login = loginTextField.getText();
        if (DatabaseManager.checkLogin(login, passwordField.getText())) {
            Utilities.showInformation("Logged in successfully!");
            TempData.updateUserStatus(true);
            TempData.setUsername(login);
            TempData.setUserID(DatabaseManager.getUserID(login));
            App.setRoot("mainscene");
        }
    }

}
