package bd;

import java.util.ArrayList;
import java.util.Random;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class MainSceneController {

    private int GRIDSIZE = 4;
    @FXML
    private Button importButton;
    
    @FXML
    private GridPane grid;
    
    @FXML
    public void initialize() {
        // grid = new GridPane();
        grid.setAlignment(Pos.CENTER); 
        ArrayList<Label> songNameList = new ArrayList<>();
        ArrayList<Label> artistNameList = new ArrayList<>();
        ArrayList<Label> albumNameList = new ArrayList<>();
        ArrayList<ImageView> imageList = new ArrayList<>();

        for (int i = 0; i < GRIDSIZE; i++) { 
            Random rand =  new Random(); 
            int rand_int = rand.nextInt(DatabaseManager.getDBLenght())+1;
            DatabaseManager.getSongInfo(rand_int, songNameList, albumNameList, artistNameList, imageList);
        }

        for (int i = 0; i < 3; i++) {
            // for (int j = 0; j < 1; j++) {
                grid.add(imageList.get(i),i,0);
                grid.add(songNameList.get(i), i, 0);
                System.out.println("Workin on " + String.valueOf(i));
                
            // }
        }
    }
}
