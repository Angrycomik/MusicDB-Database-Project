package projekt.bd;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HeaderController {

    @FXML
    private Button loginButton;

    @FXML
    private Button logoBtn;

    @FXML
    private Button signupButton;

    @FXML
    void login(ActionEvent event) throws IOException {
        App.setRoot("login");
    }
    
    @FXML
    void signup(ActionEvent event) throws IOException {
        App.setRoot("register");
    }

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
    
    void onProfileClick(){
        try {
            App.setRoot("profile");
        } catch (IOException e) {
            Utilities.showError(e);
        }
    }
    void logout() {
        TempData.updateUserStatus(false);
        TempData.setUsername(null);
        TempData.setUserID(null);
        try {
            App.setRoot("mainscene");
        } catch (IOException e) {
            Utilities.showError(e);
        }
    }

    public void initialize(){
        
        if (TempData.isUserLoggedIn()) {
            signupButton.setText("Logout");
            loginButton.setText("Profile");
            signupButton.setOnAction(event -> logout());
            loginButton.setOnAction(event -> onProfileClick());
        }
    

    }

}
