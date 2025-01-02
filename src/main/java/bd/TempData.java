package bd;

public class TempData {
    private static String _songName;
    private static String _artistName;
    private static int _year;

    public static void setData(String songName, String artistName, int year) {
        _songName = songName;
        _artistName = artistName;
        _year = year;
    }

    public static String getSongName() {
        return _songName;
    }

    public static String getArtistName() {
        return _artistName;
    }

    public static int getYear() {
        return _year;
    }

    public static void clear() {
        _songName = null;
        _artistName = null;
        _year = 0;
    }
}