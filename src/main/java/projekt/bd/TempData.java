package projekt.bd;

import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;

/**
 * This class is used for storing different data across the pages.
 */
public class TempData {
    private static String _songName;
    private static String _artistName;
    private static File _file;
    private static String _albumName;

    private static Integer _songYear;

    private static String currentUser;
    private static Integer userID;
    /**
     * This variable is used when you are visiting different user's profile page.
     */
    private static Integer visitUserID;
    /**
     * This variable is used when you are visiting different user's profile page.
     */
    private static String visitUsername;

    private static Integer _chosenID;

    private static ArrayList<VBox> _gridBoxList;
    private static ArrayList<ImageView> _gridImageList;

    private static boolean _gridIsSet = false;
    private static boolean userLoggedIn = false;

    /**
     * This method sets temporary data.
     * @param songName
     * @param artistName
     * @param albumName
     * @param file
     * @param songYear
     */
    public static void setData(String songName, String artistName, String albumName, File file, Integer songYear) {
        _songName = songName;
        _artistName = artistName;
        _albumName = albumName;
        _file = file;
        _songYear = songYear;
    }

    public static Integer getChosen() {
        return _chosenID;
    }

    public static void setChosen(Integer chosen) {
        _chosenID = chosen;
    }

    public static boolean isGridSet() {
        return _gridIsSet;
    }

    public static void setUserID(Integer id) {
        userID = id;
    }

    public static Integer getUserID() {
        return userID;
    }

    public static void setUsername(String name) {
        currentUser = name;
    }

    public static String getUsername() {
        return currentUser;
    }

    public static boolean isUserLoggedIn() {
        return userLoggedIn;
    }

    public static void updateUserStatus(boolean status) {
        userLoggedIn = status;
    }

    public static String getSongName() {
        return _songName;
    }

    public static String getArtistName() {
        return _artistName;
    }

    public static String getAlbumName() {
        return _albumName;
    }

    public static File getFile() {
        return _file;
    }

    public static void setYear(Integer _songYear) {
        TempData._songYear = _songYear;
    }

    public static int getYear() {
        return _songYear;
    }

    public static void setGrid(ArrayList<VBox> gridBoxList, ArrayList<ImageView> gridImageList) {
        _gridBoxList = gridBoxList;
        _gridImageList = gridImageList;
        _gridIsSet = true;
    }

    public static ArrayList<VBox> getGridBoxList() {
        return _gridBoxList;
    }

    public static ArrayList<ImageView> getGridImageList() {
        return _gridImageList;
    }

    /**
     * This method clears data and deletes temporary file.
     */
    public static void clear() {
        _songName = null;
        _artistName = null;
        _albumName = null;
        _file = null;
        _songYear = 0;
        File tempFile = new File("temp.jpg");
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    public static Integer getVisitUserID() {
        Integer tempUserID = visitUserID;
        visitUserID = null;
        return tempUserID;
    }

    public static void setVisitUser(Integer visitUserID, String username) {
        TempData.visitUserID = visitUserID;
        TempData.visitUsername = username;
    }

    public static String getVisitUsername() {
        return visitUsername;
    }


}