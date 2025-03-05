package projekt.bd;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

/**
 * This class is a controller for header.fxml file.
 */
public class HeaderController {

    @FXML
    private Button loginButton;

    @FXML
    private Button logoBtn;

    @FXML
    private Button signupButton;

    /**
     * Redirects user to login page.
     * @param event on mouse click event
     * @throws IOException
     */
    @FXML
    void login(ActionEvent event) throws IOException {
        App.setRoot("login");
    }
    /**
     * Redirects user to registration page.
     * @param event on mouse click event
     * @throws IOException
     */
    @FXML
    void signup(ActionEvent event) throws IOException {
        App.setRoot("register");
    }
    /**
     * Redirects user to main page.
     * @param event on mouse click event
     * @throws IOException
     */
    @FXML
    void logoBtnClick(ActionEvent event) throws IOException {
        App.setRoot("mainscene");
    }
    /**
     * Redirects user to song adding page.
     * @param event on mouse click event
     * @throws IOException
     */
    @FXML
    void onImportClick(ActionEvent event) throws IOException {
        App.setRoot("insertsong");
    }
    /**
     * Redirects user to search page.
     * @param event on mouse click event
     * @throws IOException
     */
    @FXML
    void onSearchClick(ActionEvent event) throws IOException {
        App.setRoot("search");
    }
    /**
     * Redirects user to profile page.
     * @throws IOException
     */
    void onProfileClick() {
        try {
            App.setRoot("profile");
        } catch (IOException e) {
            Utilities.showError(e);
        }
    }
    /**
     * Logs user out.
     * @throws IOException
     */
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

    /**
     * This method initializes header buttons, changing them if the user is logged in.
     */
    public void initialize() {

        if (TempData.isUserLoggedIn()) {
            signupButton.setText("Logout");
            loginButton.setText("Profile");
            signupButton.setOnAction(event -> logout());
            loginButton.setOnAction(event -> onProfileClick());
        }


    }

}
