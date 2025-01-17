package projekt.bd;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private Button loginButton;

    @FXML
    private TextField loginTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    void login(ActionEvent event) throws IOException {
        String login = loginTextField.getText();
        if(DatabaseManager.checkLogin(login, passwordField.getText())){
            Utilities.showInformation("Logged in successfully!");
            TempData.updateUserStatus(true);
            TempData.setUser(login);
            TempData.setUserID(DatabaseManager.getUserID(login));
            App.setRoot("mainscene");
        }
    }

}
