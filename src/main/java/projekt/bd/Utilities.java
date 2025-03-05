package projekt.bd;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * This class is used for methods that are used frequently.
 */
public class Utilities {
    /**
     * This method shows the error message in a dialogue window.
     * @param e exception to show
     */
    public static void showError(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText(null);
        alert.setContentText(e.getMessage());


        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(okButton);

        alert.showAndWait();
    }

    /**
     * This method shows information that artist with the given name is not found.
     * @param name artist's name
     */
    public static void showNotFound(String name) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText("Artist " + name + " not found.\n Please enter the details.");

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(okButton);

        alert.showAndWait();
    }

    /**
     * This method shows information given in the string in the dialogue window.
     * @param information message to show
     */
    public static void showInformation(String information) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(information);

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(okButton);

        alert.showAndWait();
    }

    /**
     * This method parses string for Integer.
     * @param input string to parse
     * @return integer or null.
     */
    public static Integer parseInteger(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        } else {
            return Integer.parseInt(input);
        }
    }

    /**
     * This method sets OnMouseClick for a given label and applies styling.
     * @param label label to convert
     * @param id id to redirect to
     * @param fxml fxml to redirect to
     * @param style style mode
     * @return converted label
     */
    public static Label turnLabelClickable(Label label, Integer id, String fxml, char style) {
        if (style == 'd' || style == 'p') {
            label.getStyleClass().add("labelMainPage");
            label.setWrapText(true);
        }
        label.setOnMouseClicked(event -> {
            try {
                if (style == 'p') {
                    String text = label.getText();
                    if (text.indexOf(" wrote:") > 0) {
                        TempData.setVisitUser(id, text.substring(0, text.indexOf(" wrote:")));
                    } else {
                        TempData.setVisitUser(id, text);
                    }
                }
                TempData.setChosen(id);
                System.out.println("CLicked on id: " + id);
                App.setRoot(fxml);
            } catch (IOException e) {
                showError(e);
            }
        });
        return label;
    }

    /**
     * This method changes byte array to JavaFx Image object or sets default image.
     * @param array array with image data
     * @return JavaFx image
     */
    public static Image imageFromSQL(byte[] array) {
        Image img;
        if (array != null) {
            img = new Image(new ByteArrayInputStream(array));
        } else {
            img = new Image("file:covers/logo.png");
        }
        return img;
    }

    /**
     * This method is used to change some label attributes.
     * @param message message to set on the label
     * @return label
     */
    public static Label notFoundLabel(String message) {
        Label info = new Label(message);
        info.setFont(new Font(18));
        return info;
    }

}
