package projekt.bd;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * DatabaseManager is a class with static functions that are used to interact with database.
 */
public class DatabaseManager {
    private static Connection connection;
    private static final String artistTable = "artist";
    private static final String songTable = "song";
    private static final String albumTable = "album";


    /**
     * This method sets connection to the database.
     */
    public static void setConnection() {
        String url = "jdbc:postgresql://nakedly-defiant-walrus.data-1.use1.tembo.io:5432/postgres";
        String dbase = "postgres";
        String pass = "doHDbfxz5WpIUOEb";

        try {
            connection = DriverManager.getConnection(url, dbase, pass);
        } catch (SQLException se) {
            System.out.println("Brak polaczenia z baza danych.");
            Utilities.showError(se);
        }
    }

    /**
     * This method returns connection.
     *
     * @return connection to the database
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * This method closes the connection.
     *
     * @throws SQLException
     */
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    /**
     * This method gets id of artist by name.
     *
     * @param name name of the artist
     * @return id of the artist or -1 if not found
     */
    public static int getArtistID(String name) {
        try {
            PreparedStatement pst = getConnection().prepareStatement("SELECT id FROM bd.artist where name=? ", ResultSet.CONCUR_UPDATABLE);
            pst.setString(1, name);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                rs.close();
                pst.close();
                return id;
            }
            rs.close();
            pst.close();
        } catch (Exception e) {
            Utilities.showError(e);
        }
        return -1;
    }

    /**
     * This method returns first song id from the given album id
     *
     * @param id id of the album
     * @return id of the song or -1 if not found
     */
    public static int getSongFromAlbum(int id) {
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT song_id FROM bd.info WHERE album_id = (?) ORDER BY run_order limit 1;", ResultSet.CONCUR_UPDATABLE);
            pst.setInt(1, id);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("song_id");
            }
        } catch (Exception e) {
            Utilities.showError(e);
        }
        return -1;
    }

    /**
     * This method is used to check if the given name of the item(Song, album or artist) is in the given table.
     *
     * @param name  name of the item
     * @param table table to look in
     * @return true or false depending on the search result
     * @throws Exception exception to track the errors if something goes wrong
     */
    public static boolean InDatabase(String name, String table) throws Exception {
        try {
            PreparedStatement pst = getConnection().prepareStatement("SELECT * FROM bd.inDatabase(?, ?)");
            pst.setString(1, name);
            pst.setString(2, table);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1);
            }
        } catch (SQLException e) {
            throw e;
        }
        return false;
    }

    /**
     * This method is used to insert the song. It firstly sets the temporary variable for trigger to work afterward.
     *
     * @param songName   name of the song
     * @param artistName name of the artist
     * @param year       release releaseYear
     * @return true or false depending on the result
     */
    public static boolean insertSong(String songName, String artistName, Integer year) {
        try {
            PreparedStatement stmt = getConnection().prepareStatement("SET tempvar.artist_name = '" + artistName + "'");
            stmt.execute();
            PreparedStatement pstSong = getConnection().prepareStatement("INSERT INTO bd.song (name,released) VALUES (?,?)");
            pstSong.setString(1, songName);
            if (year != null) {
                pstSong.setInt(2, year);
            } else {
                pstSong.setNull(2, java.sql.Types.INTEGER);
            }
            int rows = pstSong.executeUpdate();
            System.out.println("Polecenie -  INSERT Song - ilosc dodanych rekordow: " + rows);
            return rows > 0;
        } catch (Exception e) {
            Utilities.showError(e);
            return false;
        }
    }

    /**
     * This method is used to insert the album. It firstly sets the temporary variable for trigger to work afterward.
     *
     * @param songName   name of the song
     * @param artistName name of the artist
     * @param artistID   artist id
     * @param albumName  name of the album
     * @param year       release releaseYear
     * @param run_order  run order
     */
    public static void insertAlbum(String songName, String artistName, int artistID, String albumName, int year, Integer run_order) {
        try {
            if (InDatabase(albumName, albumTable)) {
                CallableStatement cst = getConnection().prepareCall("{call bd.insertAlbum(?,?,?,?)}");
                cst.setString(1, artistName);
                cst.setString(2, songName);
                cst.setString(3, albumName);
                if (run_order != null) {
                    cst.setInt(4, run_order);
                } else cst.setNull(4, java.sql.Types.INTEGER);
                cst.execute();
                return;
            }
            songName = songName.replace("'", "''");
            PreparedStatement ppst = getConnection().prepareStatement("SET tempvar.song_name = '" + songName + "'");
            ppst.execute();
            PreparedStatement pst = getConnection().prepareStatement("INSERT INTO bd.album (artist_id,name,released,cover) VALUES (?,?,?,?)");
            pst.setInt(1, artistID);
            pst.setString(2, albumName);
            pst.setInt(3, year); // Change with different implementation

            File tempFile = TempData.getFile();
            if (tempFile != null) {
                FileInputStream fis = new FileInputStream(tempFile);
                pst.setBinaryStream(4, fis, (int) tempFile.length());
            } else {
                pst.setNull(4, java.sql.Types.BINARY);
            }

            int rows = pst.executeUpdate();
            System.out.println("Polecenie -  INSERT Album - ilosc dodanych rekordow: " + rows);
        } catch (Exception e) {
            Utilities.showError(e);
        }
    }

    /**
     * This method is used to insert the artist. It firstly sets the temporary variable for trigger to work afterward.
     *
     * @param name      name of the artist
     * @param start     career start releaseYear
     * @param end       career end releaseYear
     * @param song_year song releaseYear for trigger to check the artist activity years
     * @return true or false depending on the result
     */
    public static boolean insertArtist(String name, Integer start, Integer end, Integer song_year) {
        try {
            PreparedStatement ppst = getConnection().prepareStatement("SET tempvar.song_year = '" + song_year + "'");
            ppst.execute();
            PreparedStatement pstArtist = DatabaseManager.getConnection().prepareStatement("INSERT INTO bd.artist (name,career_start,career_end) VALUES (?,?,?)");
            pstArtist.setString(1, name);
            if (start != null) {
                pstArtist.setInt(2, start);
            } else {
                pstArtist.setNull(2, java.sql.Types.INTEGER);
            }
            if (end != null) {
                pstArtist.setInt(3, end);
            } else {
                pstArtist.setNull(3, java.sql.Types.NULL);
            }
            int rows = pstArtist.executeUpdate();
            System.out.println("Polecenie -  INSERT Artist - ilosc dodanych rekordow: " + rows);
            return rows > 0;
        } catch (Exception e) {
            Utilities.showError(e);
            return false;
        }
    }

    /**
     * This method gets the list of all the song ids.
     *
     * @return list of ids
     */
    public static ArrayList<Integer> getSongIDs() {
        ArrayList<Integer> songIDs = new ArrayList<>();
        try {
            PreparedStatement pst = getConnection().prepareStatement("SELECT id FROM bd.song");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                songIDs.add(rs.getInt("id"));
            }
        } catch (Exception e) {
            Utilities.showError(e);
        }
        return songIDs;
    }

    /**
     * This method is used to update the grid on the main page. It sets the grid cells with an image and three labels with their onClick() methods.
     *
     * @param id        id of the song
     * @param imageList list of images
     * @return vbox that is ready to be inserted in the cell
     */
    public static VBox populateGrid(int id, ArrayList<ImageView> imageList) {
        VBox vbox = new VBox();
        try {
            CallableStatement cst = getConnection().prepareCall("{call bd.getSongInfo(?)}");
            cst.setInt(1, id);
            ResultSet rs;
            rs = cst.executeQuery();
            Image img = null;
            if (rs.next()) {
                vbox.setSpacing(5);
                vbox.setAlignment(Pos.CENTER);
                Label songLabel = new Label(rs.getString("song_name"));

                Utilities.turnLabelClickable(songLabel, id, "songpage", 'd');

                Label albumLabel = new Label(rs.getString("album_name"));
                Utilities.turnLabelClickable(albumLabel, id, "albumpage", 'd');

                Label artistLabel = new Label(rs.getString("artist_name"));
                Utilities.turnLabelClickable(artistLabel, id, "artistpage", 'd');

                vbox.getChildren().addAll(songLabel, albumLabel, artistLabel);

                img = Utilities.imageFromSQL(rs.getBytes("cover"));
            }

            imageList.add(new ImageView(img));
            rs.close();
            cst.close();
        } catch (Exception e) {
            Utilities.showError(e);
        }
        return vbox;
    }

    /**
     * This method is used to retrieve all the necessary information about the song by its id.
     *
     * @param id id of the song
     * @return object of Song class
     */
    public static Song getSongInfo(int id) {
        Song song = null;
        try {
            CallableStatement cst = getConnection().prepareCall("{call bd.getSongInfo(?)}");
            cst.setInt(1, id);
            ResultSet rs;
            rs = cst.executeQuery();
            byte[] byteArray = null;
            Image img = null;
            if (rs.next()) {
                song = new Song(rs.getString("song_name"), rs.getString("artist_name"), rs.getString("album_name"), rs.getInt("year_released"), rs.getInt("artist_song_id"));
                img = Utilities.imageFromSQL(rs.getBytes("cover"));
            }

            song.SetImage(img);
            rs.close();
            cst.close();
        } catch (Exception e) {
            Utilities.showError(e);
        }
        return song;
    }

    /**
     * This method is used to retrieve all the necessary information about the album by song id.
     *
     * @param id id of the song
     * @return object of Album class
     */
    public static Album getAlbumInfo(int id) {
        Album album = null;
        try {
            CallableStatement cst = getConnection().prepareCall("{call bd.getAlbumInfo(?)}");
            cst.setInt(1, id);

            ResultSet rs = cst.executeQuery();
            ArrayList<String> songList = null;
            ArrayList<Integer> songIDList = null;
            if (rs.next()) {

                Array songs = rs.getArray("songs");
                Array songs_id = rs.getArray("songs_id");
                if (songs != null) {
                    String[] songsString = (String[]) songs.getArray();
                    songList = new ArrayList<>(Arrays.asList(songsString));
                    Integer[] songIdsArray = (Integer[]) songs_id.getArray();
                    songIDList = new ArrayList<>(Arrays.asList(songIdsArray));
                }
                Image img = Utilities.imageFromSQL(rs.getBytes("cover"));
                album = new Album(rs.getString("album_name"), rs.getString("artist_name"), rs.getInt("year_released"), rs.getInt("album_id"), img, songList, songIDList);
            }
            rs.close();
            cst.close();
        } catch (Exception e) {
            Utilities.showError(e);
        }
        return album;
    }

    /**
     * This method is used to retrieve all the necessary information about the artist by song id.
     *
     * @param id id of the song
     * @return object of Artist class
     */
    public static Artist getArtistInfo(int id) {
        Artist artist = null;
        try {
            CallableStatement cst = getConnection().prepareCall("{call bd.getArtistInfo(?)}");
            cst.setInt(1, id);

            ResultSet rs = cst.executeQuery();
            ArrayList<String> songList = null;
            ArrayList<Integer> songsIDList = null;
            ArrayList<String> albumList = null;
            ArrayList<Integer> albumIDList = null;

            if (rs.next()) {
                Array songs = rs.getArray("songs");
                Array songs_id = rs.getArray("songs_id");
                if (songs != null) {
                    String[] songsString = (String[]) songs.getArray();
                    songList = new ArrayList<>(Arrays.asList(songsString));
                    Integer[] songsInt = (Integer[]) songs_id.getArray();
                    songsIDList = new ArrayList<>(Arrays.asList(songsInt));
                }

                Array albums = rs.getArray("albums");
                Array albums_id = rs.getArray("albums_id");
                if (albums != null) {
                    String[] albumsStr = (String[]) albums.getArray();
                    albumList = new ArrayList<>(Arrays.asList(albumsStr));
                    Integer[] albumsInt = (Integer[]) albums_id.getArray();
                    albumIDList = new ArrayList<>(Arrays.asList(albumsInt));
                }

                artist = new Artist(rs.getString("artist_name"), rs.getInt("career_start"), rs.getInt("career_end"), rs.getInt("song_count"), songList, songsIDList, albumList, albumIDList);
            }
            rs.close();
            cst.close();
        } catch (Exception e) {
            Utilities.showError(e);
        }
        return artist;
    }

    /**
     * This method is used to retrieve all the necessary information about the song by a substring of its name.
     *
     * @param name query to search
     * @return list of class Song objects
     */
    public static ArrayList<Song> searchSongs(String name) {
        ArrayList<Song> songs = new ArrayList<>();
        try {
            CallableStatement cst = getConnection().prepareCall("{call bd.searchSongs(?)}");
            cst.setString(1, name);
            ResultSet rs = cst.executeQuery();
            while (rs.next()) {
                String songName = rs.getString("song_name");
                Integer song_id = rs.getInt("song_id");
                String artistName = rs.getString("artist_name");
                Integer artist_id = rs.getInt("artist_id");
                String albumName = rs.getString("album_name");
                Integer year = rs.getInt("year_released");
                Image img = Utilities.imageFromSQL(rs.getBytes("cover"));

                Song song = new Song(songName, song_id, artistName, artist_id, albumName, year, img);

                songs.add(song);
            }
        } catch (Exception e) {
            Utilities.showError(e);
        }
        return songs;
    }

    /**
     * This method is used to retrieve all the necessary information about the song by a substring of its name.
     *
     * @param name query to search
     * @return list of class Song objects
     */
    public static ArrayList<Artist> searchArtists(String name) {
        ArrayList<Artist> artists = new ArrayList<>();
        try {
            CallableStatement cst = getConnection().prepareCall("{call bd.searchArtist(?)}");
            cst.setString(1, name);
            ResultSet rs = cst.executeQuery();

            while (rs.next()) {
                String artistName = rs.getString("artist_name");
                Integer startYear = rs.getInt("career_start");
                Integer endYear = rs.getInt("career_end");
                Integer id = rs.getInt("artist_id");

                Artist artist = new Artist(artistName, startYear, endYear, id);
                artists.add(artist);
            }
        } catch (Exception e) {
            Utilities.showError(e);
        }
        return artists;
    }

    /**
     * This method is used to retrieve all the necessary information about the albums by a substring of its name.
     *
     * @param name query to search
     * @return list of class Album objects
     */
    public static ArrayList<Album> searchAlbums(String name) {
        ArrayList<Album> albums = new ArrayList<>();
        try {
            CallableStatement cst = getConnection().prepareCall("{call bd.searchAlbums(?)}");
            cst.setString(1, name);
            ResultSet rs = cst.executeQuery();

            while (rs.next() && albums.size() < 50) {
                String albumName = rs.getString("album_name");
                String artistName = rs.getString("artist_name");
                Integer releaseYear = rs.getInt("year_released");
                Integer artistID = rs.getInt("artist_id");
                Integer albumID = rs.getInt("album_id");
                Image img = Utilities.imageFromSQL(rs.getBytes("cover"));

                Album album = new Album(albumName, artistName, releaseYear, artistID, albumID, img);
                albums.add(album);
            }
        } catch (Exception e) {
            Utilities.showError(e);
        }
        return albums;
    }

    /**
     * This method is used to register user and insert user's data into DB.
     *
     * @param username user login
     * @param password user password
     * @return true or false depending on the result
     */
    public static boolean register(String username, String password) {
        try {
            PreparedStatement pst = DatabaseManager.getConnection().prepareStatement("INSERT INTO bd.user (username,password) VALUES (?,?)");
            pst.setString(1, username);
            pst.setString(2, password);
            pst.execute();
            return true;
        } catch (Exception e) {
            Utilities.showError(e);
        }
        return false;
    }

    /**
     * This method is used to check user login and password.
     *
     * @param username user login
     * @param password user password
     * @return true if credentials are correct; false otherwise
     */
    public static boolean checkLogin(String username, String password) {
        boolean check = false;
        try {
            CallableStatement cst = DatabaseManager.getConnection().prepareCall("{call bd.checkLogin(?, ?)}");
            cst.setString(1, username);
            cst.setString(2, password);
            ResultSet rs = cst.executeQuery();
            check = rs.next();
        } catch (SQLException e) {
            Utilities.showError(e);
        }
        return check;
    }

    /**
     * This method is used to get user id by username
     *
     * @param username user login
     * @return user id
     */
    public static int getUserID(String username) {
        int id = -1;
        try {
            PreparedStatement pst = getConnection().prepareStatement("SELECT id FROM bd.user WHERE username = ?");
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
            rs.close();
        } catch (SQLException e) {
            Utilities.showError(e);
        }
        return id;
    }

    /**
     * This method is used to add or change the rating of the given item (song or album).
     *
     * @param itemID id of song or album
     * @param userID id of user
     * @param rating rating from 1 to 5
     * @param mode   mode to indicate item type, 'a' for album or 's' for song
     */
    public static void addRating(int itemID, int userID, int rating, char mode) {
        try {
            CallableStatement cst = DatabaseManager.getConnection().prepareCall("{call bd.addRating(?,?,?,?)}");
            cst.setInt(1, itemID);
            cst.setInt(2, userID);
            cst.setInt(3, rating);
            cst.setString(4, String.valueOf(mode));
            cst.execute();
        } catch (Exception e) {
            Utilities.showError(e);
        }
    }

    /**
     * This method is used to retrieve rating by item id.
     *
     * @param userID user id
     * @param itemID id of song or album
     * @param mode   mode to indicate item type, 'a' for album or 's' for song
     * @return integer rating from 1 to 5
     */
    public static Integer getRating(int userID, int itemID, char mode) {
        try {
            PreparedStatement pst;
            if (mode == 's') {
                pst = getConnection().prepareStatement("SELECT rating FROM bd.songrating WHERE user_id = ? AND song_id = ?");
            } else if (mode == 'a') {
                pst = getConnection().prepareStatement("SELECT rating FROM bd.albumrating WHERE user_id = ? AND album_id = ?");
            } else {
                return -1;
            }
            pst.setInt(1, userID);
            pst.setInt(2, itemID);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt("rating");
            }
        } catch (SQLException e) {
            Utilities.showError(e);
        }
        return null;
    }

    /**
     * This method is used to get and calculate the average rating of an item (song or album)
     *
     * @param itemID id of song or album
     * @param mode   mode to indicate item type, 'a' for album or 's' for song
     * @return
     */
    public static Double getAverageRating(int itemID, char mode) {
        try {
            CallableStatement cst = getConnection().prepareCall("{call bd.getAverageRating(?, ?)}");
            cst.setInt(1, itemID);
            cst.setString(2, String.valueOf(mode));
            ResultSet rs = cst.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            Utilities.showError(e);
        }
        return null;
    }

    /**
     * This method is used to get playlist with songs created by user with given user id.
     *
     * @param userID id of user
     * @return Pair object with a list of playlist names and playlist ids
     */
    public static Pair<ArrayList<String>, ArrayList<Integer>> getUserPlaylists(int userID) {
        ArrayList<String> playlistNames = new ArrayList<>();
        ArrayList<Integer> playlistID = new ArrayList<>();
        try {
            PreparedStatement pst = getConnection().prepareStatement("SELECT * FROM bd.getUserPlaylists(?)");
            pst.setInt(1, userID);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                playlistID.add(rs.getInt("playlist_id"));
                playlistNames.add(rs.getString("playlist_name"));
            }
        } catch (SQLException e) {
            Utilities.showError(e);
        }
        return new Pair<>(playlistNames, playlistID);
    }

    /**
     * This method is used to add a playlist.
     *
     * @param name name of a playlist
     * @return id of the created playlist
     */
    public static int addPlaylist(String name) {
        try {
            PreparedStatement pst = getConnection().prepareStatement("INSERT INTO bd.playlist (name,user_id) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, name);
            pst.setInt(2, TempData.getUserID());
            int rows = pst.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            Utilities.showError(e);
        }
        return -1;
    }

    /**
     * This method is used to add a song to playlist.
     *
     * @param playlistID id of a playlist
     * @param songID     id of a song
     */
    public static void addSongToPlaylist(int playlistID, int songID) {
        try {
            PreparedStatement pst = getConnection().prepareStatement("INSERT INTO bd.playlistsong (playlist_id,song_id) VALUES (?,?)");
            pst.setInt(1, playlistID);
            pst.setInt(2, songID);
            pst.execute();
        } catch (Exception e) {
            Utilities.showError(e);
        }
    }

    /**
     * This method is used to get songs from a given playlist by playlist id.
     *
     * @param playlistID id of the playlist
     * @return Pair object with a list of song names and song ids
     */
    public static Pair<ArrayList<String>, ArrayList<Integer>> getSongsFromPlaylist(int playlistID) {
        ArrayList<String> playlistNames = new ArrayList<>();
        ArrayList<Integer> songIDs = new ArrayList<>();
        try {
            PreparedStatement pst = getConnection().prepareStatement("SELECT * FROM bd.getSongsFromPlaylist(?)");
            pst.setInt(1, playlistID);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("song_id");
                String songName = rs.getString("song_name");
                String artistName = rs.getString("artist_name");

                playlistNames.add(songName + "\nby " + artistName);
                songIDs.add(id);
            }

        } catch (SQLException e) {
            Utilities.showError(e);
        }
        return new Pair<>(playlistNames, songIDs);
    }

    /**
     * This method is used to add a review.
     *
     * @param reviewText     text of the review
     * @param song_artist_id id of the song
     * @return true or false depending on the result
     */
    public static boolean addReview(String reviewText, int song_artist_id) {
        try {
            PreparedStatement pst = DatabaseManager.getConnection().prepareStatement("INSERT INTO bd.review (song_id, user_id, review_text) VALUES (?, ?, ?)");
            pst.setInt(1, song_artist_id);
            pst.setInt(2, TempData.getUserID());
            pst.setString(3, reviewText);
            int rows = pst.executeUpdate();

            return rows > 0;
        } catch (Exception e) {
            Utilities.showError(e);
        }
        return false;
    }

    /**
     * This method is used to get the reviews of a song by song id.
     *
     * @param song_artist_id id of a song
     * @return list of objects of class Review
     */
    public static ArrayList<Review> getReviews(int song_artist_id) {
        ArrayList<Review> reviews = new ArrayList<>();
        try {
            PreparedStatement pst = getConnection().prepareStatement("SELECT user_id, username, review_text FROM bd.reviews where song_artist_id = (?)");
            pst.setInt(1, song_artist_id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int userID = rs.getInt("user_id");
                String username = rs.getString("username");
                String reviewText = rs.getString("review_text");

                Review review = new Review(userID, username, reviewText);
                reviews.add(review);
            }

        } catch (SQLException e) {
            Utilities.showError(e);
        }
        return reviews;
    }

    /**
     * This method is used for following other users.
     *
     * @param id id of the user who we want to follow
     * @return true or false depending on the result
     */
    public static boolean followUser(int id) {
        try {
            int userID = TempData.getUserID();
            PreparedStatement pst = DatabaseManager.getConnection().prepareStatement("INSERT INTO bd.follow (user_id, follows_id) VALUES (?, ?)");
            pst.setInt(1, userID);
            pst.setInt(2, id);
            int rows = pst.executeUpdate();

            return rows > 0;
        } catch (Exception e) {
            Utilities.showError(e);
            return false;
        }
    }

    /**
     * This method is used to check if current user follows another one.
     *
     * @param userID      id of user
     * @param visitUserID id of user who want to check our status with
     * @return true or false depending on the result
     */
    public static boolean isFollowing(Integer userID, Integer visitUserID) {
        try {

            PreparedStatement pst = DatabaseManager.getConnection().prepareStatement("SELECT exists (select 1 from bd.follow where user_id = ? and follows_id = ?)");

            pst.setInt(1, userID);
            pst.setInt(2, visitUserID);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1);
            }
        } catch (Exception e) {
            Utilities.showError(e);
        }
        return false;
    }

    /**
     * This method is used to get user follow list.
     *
     * @param userID id of the user
     * @return Pair object with a list of usernames and their ids
     */
    public static Pair<ArrayList<String>, ArrayList<Integer>> getFollowingUsers(int userID) {
        ArrayList<String> usernames = new ArrayList<>();
        ArrayList<Integer> followsIDs = new ArrayList<>();
        try {
            CallableStatement cst = getConnection().prepareCall("{ call bd.getFollowList(?) }");
            cst.setInt(1, userID);
            ResultSet rs = cst.executeQuery();

            while (rs.next()) {
                String username = rs.getString("username");
                int follows_id = rs.getInt("follows_id");

                usernames.add(username);
                followsIDs.add(follows_id);
            }
        } catch (Exception e) {
            Utilities.showError(e);
        }

        return new Pair<>(usernames, followsIDs);
    }

    /**
     * This method searches artists that have N songs with rating above N.
     * @param rating minimal rating
     * @param min minimal number of songs
     * @return  list of artists
     */
    public static ArrayList<Artist> getArtistsWithRatingsAbove(int rating, int min) {
        ArrayList<Artist> artists = new ArrayList<>();
        try {
            CallableStatement cst = getConnection().prepareCall("{call projekt.getArtistsWithRatingsAbove(?, ?)}");
            cst.setInt(1, rating);
            cst.setInt(2, min);
            ResultSet rs = cst.executeQuery();

            while (rs.next()) {
                String artistName = rs.getString("artist_name");
                int artistId = rs.getInt("artist_id");
                int songCount = rs.getInt("song_count");

                Artist artist = new Artist(artistName, artistId, songCount);
                artists.add(artist);
            }
            rs.close();
            cst.close();
        } catch (Exception e) {
            Utilities.showError(e);
        }
        return artists;
    }
}
