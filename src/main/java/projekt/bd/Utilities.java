package projekt.bd;

import java.io.IOException;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class Utilities {

        public static void showError(Exception e){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText(null);
        alert.setContentText(e.getMessage());


        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(okButton);

        alert.showAndWait();
    }
    public static void showNotFound(String name){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText("Artist " + name +  " not found");

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(okButton);

        alert.showAndWait();
    }
    public static void showInformation(String information){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(information);

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(okButton);

        alert.showAndWait();
    }

    public static Integer parseInteger(String input) { 
        if (input == null || input.trim().isEmpty()) { 
            return null; 
        }else { 
            return Integer.parseInt(input); 
        } 
    }
    public static Label turnLabelClickable(Label label, int id,String fxml,String style) {
        if(style.equals("d")){
            label.getStyleClass().add("labelMainPage");
        }
        label.setOnMouseClicked(event -> {
            try {
                TempData.setChosen(id);
                App.setRoot(fxml);
            } catch (IOException e) {
                showError(e);
            }
        });
        return label;
    }
}
