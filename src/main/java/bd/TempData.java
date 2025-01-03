package bd;
import java.io.File;
public class TempData {
    private static String _songName;
    private static String _artistName;
    private static File _file;
    private static String _albumName;
    private static Integer _songYear;
    private static Integer _albumYear;

    public static void setData(String songName, String artistName,String albumName,File file, Integer songYear,Integer albumYear) {
        _songName = songName;
        _artistName = artistName;
        _albumName = albumName;
        _file = file;
        _songYear = songYear;
        _albumYear = albumYear;
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

    public static void clear() {
        _songName = null;
        _artistName = null;
        _albumName = null;
        _file = null;
        _songYear = 0;
        _albumYear = null;
    }
}