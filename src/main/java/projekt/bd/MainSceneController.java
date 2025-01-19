package projekt.bd;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class MainSceneController {

    private int GRIDSIZE = 12;
    @FXML
    private Button importButton;
    
    @FXML
    private GridPane grid;
    
    @FXML
    private BorderPane borderPane;

    ArrayList<Integer> ids;
    
    @FXML
    public void initialize() {
        ids = DatabaseManager.getSongIDs();
        if(ids.size() > 12){
            if (!TempData.isGridSet()) {
                makeNewGrid();
            } else {
                makeGridFromTempData();
            }
        }

    }

    @FXML
    void refreshButton(ActionEvent event) {
        makeNewGrid();
    }
    
    private void makeNewGrid() {
        clearGrid();
        ArrayList<VBox> boxList = new ArrayList<>();
        ArrayList<ImageView> imageList = new ArrayList<>();

        ArrayList<Integer> songList = new ArrayList<>(randomList());
    
        for (int i : songList) {
            boxList.add(DatabaseManager.populateGrid(i, imageList));
        }
    
        populateGrid(grid, boxList,imageList);
        TempData.setGrid(boxList, imageList);
    }
    
    private void makeGridFromTempData() {
        ArrayList<VBox> boxList = TempData.getGridBoxList();
        ArrayList<ImageView> imageList = TempData.getGridImageList();
    
        populateGrid(grid, boxList, imageList);
    }
    
    private void populateGrid(GridPane grid, ArrayList<VBox> boxList, ArrayList<ImageView> imageList) {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                grid.add(imageList.get(count), i, j);
                grid.add(boxList.get(count), i, j);

                count++;
            }
        }
    }
    
    private List<Integer> randomList() {
        Collections.shuffle(ids);
        return ids.subList(0,GRIDSIZE);
//        ArrayList<Integer> songList = new ArrayList<>();
//        Random rand = new Random();
//        int size = ids.size();
//        while (songList.size() < GRIDSIZE) {
//            int randInt = rand.nextInt(size) + 1;
//            if (!songList.contains(randInt)) {
//                songList.add(randInt);
//            }
//        }
    }
    private void clearGrid() {
        grid.getChildren().clear();
    }

}
