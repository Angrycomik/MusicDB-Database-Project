package bd;

import java.util.ArrayList;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class MainSceneController {

    private int GRIDSIZE = 12;
    @FXML
    private Button importButton;
    
    @FXML
    private GridPane grid;
    
    @FXML
    private BorderPane borderPane;
    
    @FXML
    public void initialize() {
        // grid.setAlignment(Pos.CENTER); 
        if (!TempData.isGridSet()) {
            makeNewGrid();
        } else {
            makeGridFromTempData();
        }
    }

    @FXML
    void refreshButton(ActionEvent event) {
        makeNewGrid();
    }
    
    private void makeNewGrid() {
        clearGrid();
        ArrayList<Label> songNameList = new ArrayList<>();
        ArrayList<Label> artistNameList = new ArrayList<>();
        ArrayList<Label> albumNameList = new ArrayList<>();
        ArrayList<ImageView> imageList = new ArrayList<>();
        
        int size = DatabaseManager.getDBLenght();
        ArrayList<Integer> songList = randomList(size);
    
        for (int i : songList) {
            DatabaseManager.populateGrid(i, songNameList, albumNameList, artistNameList, imageList);
        }
    
        populateGrid(grid, songNameList, imageList);
        TempData.setGrid(songNameList, artistNameList, albumNameList, imageList);
    }
    
    private void makeGridFromTempData() {
        ArrayList<Label> songNameList = TempData.getGridSongNameList();
        ArrayList<Label> artistNameList = TempData.getGridArtistNameList();
        ArrayList<Label> albumNameList = TempData.getGridAlbumNameList();
        ArrayList<ImageView> imageList = TempData.getGridImageList();
    
        populateGrid(grid, songNameList, imageList);
    }
    
    private void populateGrid(GridPane grid, ArrayList<Label> songNameList, ArrayList<ImageView> imageList) {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                grid.add(imageList.get(count), i, j);
                grid.add(songNameList.get(count), i, j);
                count++;
            }
        }
    }
    
    private ArrayList<Integer> randomList(int size) {
        ArrayList<Integer> songList = new ArrayList<>();
        Random rand = new Random();
    
        while (songList.size() < GRIDSIZE) {
            int randInt = rand.nextInt(size) + 1;
            if (!songList.contains(randInt)) {
                songList.add(randInt);
            }
        }
        return songList;
    }
    private void clearGrid() {
        grid.getChildren().clear();
    }

}
