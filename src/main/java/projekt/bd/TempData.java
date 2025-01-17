package projekt.bd;
import java.io.File;
import java.util.ArrayList;

import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
public class TempData {
    private static String _songName;
    private static String _artistName;
    private static File _file;
    private static String _albumName;
    private static Integer _songYear;
    private static Integer _albumYear;

    private static String currentUser;
    private static Integer userID;
    
    private static Song song;

    private static Integer _chosenID;

    private static ArrayList<VBox> _gridBoxList;
    private static ArrayList<ImageView> _gridImageList;

    private static boolean _gridIsSet = false;
    private static boolean userLoggedIn = false;

    public static void setData(String songName, String artistName,String albumName,File file, Integer songYear,Integer albumYear) {
        _songName = songName;
        _artistName = artistName;
        _albumName = albumName;
        _file = file;
        _songYear = songYear;
        _albumYear = albumYear;
    }

    public static Song getSong() {
        return song;
    }

    public static Integer getChosen() {
        return _chosenID;
    }
    public static void setChosen(Integer chosen) {
        _chosenID= chosen;
    }

    public static void setSong(String songName,String artistName,String albumName,Integer albumYear) {
        song = new Song(songName, artistName, albumName, albumYear);
    }

    public static boolean isGridSet() {
        return _gridIsSet;
    }
    public static void setUserID(Integer id){
        userID = id;
    }
    public static Integer getUserID(){
        return userID;
    }
    public static void setUser(String name){
        currentUser = name;
    }
    public static String getUser(){
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

    public static int getSongYear() {
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

    public static void clear() {
        _songName = null;
        _artistName = null;
        _albumName = null;
        _file = null;
        _songYear = 0;
        _albumYear = null;
        File tempFile = new File("temp.jpg");
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }



}