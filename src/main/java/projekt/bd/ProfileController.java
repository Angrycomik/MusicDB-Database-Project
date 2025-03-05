package projekt.bd;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.ArrayList;
/**
 * This class is a controller for profile.fxml file.
 */
public class ProfileController {

    @FXML
    private Button followBtn;

    @FXML
    private VBox followListBox;

    @FXML
    private VBox playlistBox;

    @FXML
    private Label usernameLabel;
    private Integer tempUserID;
    /**
     * This method is initializing elements during the page loading, such as label names and updates pane's content, also retrieves the information from the database.
     */
    public void initialize() {
        tempUserID = TempData.getVisitUserID();
        String username = TempData.getVisitUsername();
        if (tempUserID == null) {
            tempUserID = TempData.getUserID();
            username = TempData.getUsername();
        }
        if (TempData.isUserLoggedIn() && !tempUserID.equals(TempData.getUserID())) {
            boolean isFollowing = DatabaseManager.isFollowing(TempData.getUserID(), tempUserID);
            followBtn.setVisible(true);
            if (isFollowing) {
                followBtn.setDisable(true);
                followBtn.setText("Followed");
            }
        }

        System.out.println("search for id: " + tempUserID);
        usernameLabel.setText(username + "'s page");
        updatePlaylistBox(tempUserID);
        updateFollowListBox(tempUserID);
    }

    /**
     * This method is used to update playlist pane's content.
     * @param id id of a user
     */
    void updatePlaylistBox(Integer id) {
        playlistBox.getChildren().clear();
        DatabaseManager.getUserPlaylists(id);
        Pair<ArrayList<String>, ArrayList<Integer>> pair = DatabaseManager.getUserPlaylists(id);
        ArrayList<String> playlists = pair.getKey();
        ArrayList<Integer> playlistIDs = pair.getValue();
        for (int i = 0; i < playlistIDs.size(); i++) {
            Box box = new Box(playlists.get(i), playlistIDs.get(i), 'p');
            int finalI = i;
            box.getChildren().get(0).setOnMouseClicked(event -> {
                displaySongs(playlistIDs.get(finalI));
            });
            playlistBox.getChildren().add(box);
        }
    }

    /**
     * This method is used to display playlist's content from a song list.
     * @param playlistID id of playlist to display.
     */
    void displaySongs(int playlistID) {
        playlistBox.getChildren().clear();
        Pair<ArrayList<String>, ArrayList<Integer>> pair = DatabaseManager.getSongsFromPlaylist(playlistID);
        ArrayList<String> names = pair.getKey();
        ArrayList<Integer> songsIDList = pair.getValue();
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            System.out.println("Name is " + name);
            Integer songID = songsIDList.get(i);
            Box box = new Box(name, songID, 's');
            box.setPrefHeight(65);
            playlistBox.getChildren().add(box);
        }
    }

    /**
     * This method adds user to the follow list.
     * @param event on mouse click event
     */
    @FXML
    void follow(ActionEvent event) {
        if (DatabaseManager.followUser(tempUserID)) {
            Utilities.showInformation("User followed!");
            followBtn.setDisable(true);
            followBtn.setText("Followed");
        }
    }

    /**
     * This method is used for retrieving given user's follow list.
     * @param id id of user
     */
    private void updateFollowListBox(int id) {
        Pair<ArrayList<String>, ArrayList<Integer>> following = DatabaseManager.getFollowingUsers(id);
        followListBox.getChildren().clear();
        ArrayList<String> names = following.getKey();
        ArrayList<Integer> IDs = following.getValue();
        for (int i = 0; i < IDs.size(); i++) {
            String username = names.get(i);
            Integer userID = IDs.get(i);
            Box box = new Box(username, userID, 'f');
            followListBox.getChildren().add(box);
        }
    }

}
