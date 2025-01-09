package bd;
import java.io.File;
import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
public class TempData {
    private static String _songName;
    private static String _artistName;
    private static File _file;
    private static String _albumName;
    private static Integer _songYear;
    private static Integer _albumYear;

    private static ArrayList<Label> gridSongNameList;
    private static ArrayList<Label> gridArtistNameList;
    private static ArrayList<Label> gridAlbumNameList;
    private static ArrayList<ImageView> gridImageList;

    private static boolean gridIsSet = false;

    public static void setData(String songName, String artistName,String albumName,File file, Integer songYear,Integer albumYear) {
        _songName = songName;
        _artistName = artistName;
        _albumName = albumName;
        _file = file;
        _songYear = songYear;
        _albumYear = albumYear;
    }
    public static boolean isGridSet() {
        return gridIsSet;
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
    public static void setGrid(ArrayList<Label> gridSongNameList, ArrayList<Label> gridArtistNameList,
                               ArrayList<Label> gridAlbumNameList,   ArrayList<ImageView> gridImageList) {
    TempData.gridSongNameList = gridSongNameList;
    TempData.gridArtistNameList = gridArtistNameList;
    TempData.gridAlbumNameList = gridAlbumNameList;
    TempData.gridImageList = gridImageList;
    gridIsSet = true;
    }

    public static ArrayList<Label> getGridSongNameList() {
        return gridSongNameList;
    }

    public static ArrayList<Label> getGridArtistNameList() {
        return gridArtistNameList;
    }

    public static ArrayList<Label> getGridAlbumNameList() {
        return gridAlbumNameList;
    }

    public static ArrayList<ImageView> getGridImageList() {
        return gridImageList;
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