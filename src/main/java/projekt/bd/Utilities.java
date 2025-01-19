package projekt.bd;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

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
        alert.setContentText("Artist " + name +  " not found.\n Please enter the details.");

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
    public static Label turnLabelClickable(Label label, Integer id,String fxml,char style) {
        if(style =='d'){
            label.getStyleClass().add("labelMainPage");
            label.setWrapText(true);
        }else if (style == 'p'){
            label.getStyleClass().add("labelMainPage");
            label.setWrapText(true);
        }
        label.setOnMouseClicked(event -> {
            try {
                if(style == 'p') {
                    String text = label.getText();
                    if(text.indexOf(" wrote:")>0) {
                        TempData.setVisitUser(id,text.substring(0,text.indexOf(" wrote:")));
                    }else{
                        TempData.setVisitUser(id,text);
                    }
                }
                TempData.setChosen(id);
                System.out.println("CLicked on id: " + String.valueOf(id));
                App.setRoot(fxml);
            } catch (IOException e) {
                showError(e);
            }
        });
        return label;
    }
    public static Image imageFromSQL(byte[] array){
            Image img;
            if(array !=null){
                img = new Image(new ByteArrayInputStream(array));
            }else{
                System.out.println("Working Directory = " + System.getProperty("user.dir"));

                img = new Image("file:covers/logo.png");
            }
            return img;
    }
    public static Label notFoundLabel(String message){
        Label info = new Label(message);
        info.setFont(new Font(18));
        return info;
    }

}
