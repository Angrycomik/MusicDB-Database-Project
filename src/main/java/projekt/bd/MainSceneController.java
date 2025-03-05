package projekt.bd;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * This class is a controller for mainscene.fxml file.
 */
public class MainSceneController {

    private final int GRIDSIZE = 12;
    @FXML
    private Button importButton;

    @FXML
    private GridPane grid;

    @FXML
    private BorderPane borderPane;

    ArrayList<Integer> ids;

    /**
     * This method checks the condition to create the grid and retrieves ids from database.
     */
    @FXML
    public void initialize() {
        ids = DatabaseManager.getSongIDs();
        if (ids.size() > GRIDSIZE) {
            if (!TempData.isGridSet()) {
                makeNewGrid();
            } else {
                makeGridFromTempData();
            }
        }

    }

    /**
     * This method refreshes grid.
     * @param event on mouse click event
     */
    @FXML
    void refreshButton(ActionEvent event) {
        if (ids.size() > GRIDSIZE) {
            makeNewGrid();
        }
    }

    /**
     * This method makes new grid.
     */
    private void makeNewGrid() {
        clearGrid();
        ArrayList<VBox> boxList = new ArrayList<>();
        ArrayList<ImageView> imageList = new ArrayList<>();

        ArrayList<Integer> songList = new ArrayList<>(randomList());

        for (int i : songList) {
            boxList.add(DatabaseManager.populateGrid(i, imageList));
        }

        populateGrid(grid, boxList, imageList);
        TempData.setGrid(boxList, imageList);
    }

    /**
     * This method retrieves grid from the memory.
     */
    private void makeGridFromTempData() {
        ArrayList<VBox> boxList = TempData.getGridBoxList();
        ArrayList<ImageView> imageList = TempData.getGridImageList();

        populateGrid(grid, boxList, imageList);
    }

    /**
     * This method adds obtained data into grid cells.
     * @param grid grid object
     * @param boxList list of boxes with labels
     * @param imageList image to set
     */
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

    /**
     * This method shuffles list of ids before making a new grid.
     * @return sublist of ids to create grid from.
     */
    private List<Integer> randomList() {
        Collections.shuffle(ids);
        return ids.subList(0, GRIDSIZE);
    }

    /**
     * This method is used to clear the grid.
     */
    private void clearGrid() {
        grid.getChildren().clear();
    }

}
