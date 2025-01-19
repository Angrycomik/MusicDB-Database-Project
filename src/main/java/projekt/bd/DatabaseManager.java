package projekt.bd;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class DatabaseManager {
    private static Connection connection;
    private static String artistTable = "artysta";
    private static String songTable = "piosenka";
    private static String albumTable = "plyta";


    public static void setConnection() throws SQLException {
        String url = "jdbc:postgresql://nakedly-defiant-walrus.data-1.use1.tembo.io:5432/postgres";
        String dbase = "postgres";
        String pass = "doHDbfxz5WpIUOEb";

        try {
            connection = DriverManager.getConnection(url, dbase, pass);
          } catch (SQLException se) {
            System.out.println("Brak polaczenia z baza danych.");

          }
    }

    public static Connection getConnection(){
          return connection;
    }
    
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
    public static int getAlbumID(String name,int artistID){
        try { 
            PreparedStatement pst = getConnection().prepareStatement("SELECT id FROM projekt.plyta where nazwa=? and artysta_id=? ",ResultSet.CONCUR_UPDATABLE);
            pst.setString(1, name);
            pst.setInt(2,artistID);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                rs.close();
                pst.close();
                return id;
            }
    
            rs.close();
            pst.close();
        }catch(Exception e){
            Utilities.showError(e);
        }
        return -1;
    }
    public static int getArtistID(String name){
        try { 
            PreparedStatement pst = getConnection().prepareStatement("SELECT id FROM projekt.artysta where nazwa=? ",ResultSet.CONCUR_UPDATABLE);
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
        }catch(Exception e){
            Utilities.showError(e);
        }
        return -1;
    }

    public static int getSongFromAlbum(int id){
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT song_id FROM projekt.info WHERE plyta_id = (?) ORDER BY run_order limit 1;",ResultSet.CONCUR_UPDATABLE);
            pst.setInt(1, id);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("song_id");
            }
        }catch(Exception e){
            Utilities.showError(e);
        }
        return -1;
    }

    public static boolean InDatabase(String name,String table) throws Exception{
        try { 
            PreparedStatement pst = getConnection().prepareStatement("SELECT * FROM projekt.inDatabase(?, ?)");
            pst.setString(1, name);
            pst.setString(2, table);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1);
            }
            }
            catch(SQLException e)  {
                throw e;
            }
            return false;
        }

    public static boolean insertSong(String songName, String artistName,Integer year){
        try {
            PreparedStatement stmt = getConnection().prepareStatement("SET tempvar.artist_name = '" + artistName + "'");
            stmt.execute();
            PreparedStatement pstSong = getConnection().prepareStatement( "INSERT INTO projekt.piosenka (nazwa,napisana) VALUES (?,?)" );
            pstSong.setString(1, songName);
            if(year != null){
                pstSong.setInt(2, year);
            }else{
                pstSong.setNull(2, java.sql.Types.INTEGER);
            }
            int rows = pstSong.executeUpdate();
            System.out.println("Polecenie -  INSERT Song - ilosc dodanych rekordow: " + String.valueOf(rows));
            return true;
        }catch(Exception e){
            Utilities.showError(e);
            return false;
        }
    }

    public static void insertAlbum(String songName,String artistName, int artistID,String albumName,int year,Integer run_order){
        try { 
            if(InDatabase(albumName, albumTable)){
                CallableStatement cst = getConnection().prepareCall( "{call projekt.insertAlbum(?,?,?,?)}"); 
                cst.setString(1, artistName);
                cst.setString(2,songName);
                cst.setString(3,albumName);
                if(run_order!= null){
                    cst.setInt(4, run_order);
                }else cst.setNull(4, java.sql.Types.INTEGER);
                cst.execute();
                return;
            }
            songName = songName.replace("'","''");
            PreparedStatement ppst = getConnection().prepareStatement("SET tempvar.song_name = '" + songName + "'");
            ppst.execute();
            PreparedStatement pst = getConnection().prepareStatement( "INSERT INTO projekt.plyta (artysta_id,nazwa,napisana,okladka) VALUES (?,?,?,?)" );
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
            System.out.println("Polecenie -  INSERT Album - ilosc dodanych rekordow: " + String.valueOf(rows));
        }catch(Exception e){
            Utilities.showError(e);
        }
    }

    public static boolean insertArtist(String name, Integer start,Integer end,Integer song_year){
        try {
            PreparedStatement ppst = getConnection().prepareStatement("SET tempvar.song_year = '" + song_year + "'");
            ppst.execute();
            PreparedStatement pstArtist = DatabaseManager.getConnection().prepareStatement( "INSERT INTO projekt.artysta (nazwa,poczatek_kariery,koniec_kariery) VALUES (?,?,?)" );
            pstArtist.setString(1, name);
            if(start != null){
                pstArtist.setInt(2,start);
            }else{
                pstArtist.setNull(2, java.sql.Types.INTEGER);
            }
            if(end != null){
                pstArtist.setInt(3,end);
            }else{
                pstArtist.setNull(3, java.sql.Types.NULL);
            }
            int rows = pstArtist.executeUpdate();
            System.out.println("Polecenie -  INSERT Artist - ilosc dodanych rekordow: " + String.valueOf(rows));
            return true;
        }catch(Exception e){
            Utilities.showError(e);
            return false;
        }
    }
    
    public static ArrayList<Integer> getSongIDs(){
        ArrayList<Integer> songIDs = new ArrayList<>();
        try{
           PreparedStatement pst = getConnection().prepareStatement("SELECT id FROM projekt.piosenka");
           ResultSet rs = pst.executeQuery();
           while (rs.next()) {
               songIDs.add(rs.getInt("id"));
           }
        } catch (Exception e) {
            Utilities.showError(e);            }
        return songIDs;
    }


    public static VBox populateGrid(int id,ArrayList<ImageView> imageList){
        int size = -1;
        VBox vbox = new VBox();
        try {
            CallableStatement cst = getConnection().prepareCall( "{call projekt.getSongInfo(?)}" );
            cst.setInt(1, id);  
            ResultSet rs ;
            rs = cst.executeQuery();
            Image img = null;
            if(rs.next()){
                vbox.setSpacing(5); 
                vbox.setAlignment(Pos.CENTER); 
                Label songLabel = new Label(rs.getString("nazwa_piosenki"));

                Utilities.turnLabelClickable(songLabel,id,"songpage",'d');

                Label albumLabel = new Label(rs.getString("nazwa_plyty"));
                Utilities.turnLabelClickable(albumLabel,id,"albumpage",'d');

                Label artistLabel = new Label(rs.getString("nazwa_artysty"));
                Utilities.turnLabelClickable(artistLabel,id,"artistpage",'d');

                vbox.getChildren().addAll(songLabel, albumLabel, artistLabel);

                img = Utilities.imageFromSQL(rs.getBytes("okladka"));
            }

            imageList.add(new ImageView(img));
            rs.close();      
            cst.close();
        } catch (Exception e) {
            Utilities.showError(e);
        }
        return vbox;
    }

    public static Song getSongInfo(int id){
        Song song = null;
        try {
            CallableStatement cst = getConnection().prepareCall( "{call projekt.getSongInfo(?)}" );
            cst.setInt(1, id);  
            ResultSet rs ;
            rs = cst.executeQuery();
            byte[] byteArray = null;
            Image img = null;
            if(rs.next()){
                song = new Song(rs.getString("nazwa_piosenki"), rs.getString("nazwa_artysty"), rs.getString("nazwa_plyty"), rs.getInt("rok_napisania"),rs.getInt("artysta_piosenka_id"));
                img = Utilities.imageFromSQL(rs.getBytes("okladka"));
            }

            song.SetImage(img);
            rs.close();      
            cst.close();
        } catch (Exception e) {
            Utilities.showError(e);
        }
        return song;
    }

    public static Album getAlbumInfo(int id) {
        Album album = null;
        try {
            CallableStatement cst = getConnection().prepareCall("{call projekt.getAlbumInfo(?)}");
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
                Image img = Utilities.imageFromSQL(rs.getBytes("okladka"));
                album = new Album(rs.getString("nazwa_plyty"), rs.getString("nazwa_artysty"), rs.getInt("rok_napisania"),rs.getInt("album_id"), img, songList,songIDList);
            }

            rs.close();
            cst.close();
        } catch (Exception e) {
            Utilities.showError(e);
        }
        return album;
    }

    public static Artist getArtistInfo(int id) {
        Artist artist = null;
        try {
            CallableStatement cst = getConnection().prepareCall("{call projekt.getArtistInfo(?)}");
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
    
                artist = new Artist(rs.getString("nazwa_artysty"), rs.getInt("poczatek_kariery"), rs.getInt("koniec_kariery"),rs.getInt("song_count"), songList,songsIDList, albumList, albumIDList);
            }
            rs.close();
            cst.close();
        } catch (Exception e) {
            Utilities.showError(e); 
        }
        return artist;
    }
    


    public static ArrayList<Song> searchSongs(String name){
        ArrayList<Song> songs = new ArrayList<>();
        try {
            CallableStatement cst = getConnection().prepareCall( "{call projekt.searchSongs(?)}" );
            cst.setString(1, name);
            ResultSet rs = cst.executeQuery();
            while (rs.next()) {
                String songName = rs.getString("nazwa_piosenki");
                Integer song_id = rs.getInt("piosenka_id");
                String artistName = rs.getString("nazwa_artysty");
                Integer artist_id = rs.getInt("artysta_id");
                String albumName = rs.getString("nazwa_plyty");
                Integer year = rs.getInt("rok_napisania");
                Image img = Utilities.imageFromSQL(rs.getBytes("okladka"));

                Song song = new Song(songName,song_id, artistName,artist_id, albumName, year,img);

                songs.add(song);
            }
        } catch (Exception e) {
            Utilities.showError(e);
        }
        return songs;
    }

    public static ArrayList<Artist> searchArtists(String name) {
        ArrayList<Artist> artists = new ArrayList<>();
        try {
            CallableStatement cst = getConnection().prepareCall("{call projekt.searchArtist(?)}");
            cst.setString(1, name);
            ResultSet rs = cst.executeQuery();
            
            while (rs.next()) {
                String artistName = rs.getString("nazwa_artysty");
                Integer startYear = rs.getInt("poczatek_kariery");
                Integer endYear = rs.getInt("koniec_kariery");
                Integer id = rs.getInt("artysta_id");
    
                Artist artist = new Artist(artistName, startYear, endYear,id);
                artists.add(artist);
            }
        } catch (Exception e) {
            Utilities.showError(e);
        }
        return artists;
    }

    public static ArrayList<Album> searchAlbums(String name) {
        ArrayList<Album> albums = new ArrayList<>();
        try {
            CallableStatement cst = getConnection().prepareCall("{call projekt.searchAlbums(?)}");
            cst.setString(1, name);
            ResultSet rs = cst.executeQuery();
            
            while (rs.next() && albums.size()<50) {
                String albumName = rs.getString("nazwa_albumu");
                String artistName = rs.getString("nazwa_artysty");
                Integer releaseYear = rs.getInt("rok_napisania");
                Integer artistID = rs.getInt("artysta_id");
                Integer albumID = rs.getInt("plyta_id");
                Image img = Utilities.imageFromSQL(rs.getBytes("okladka"));

                Album album = new Album(albumName, artistName, releaseYear,artistID,albumID,img);
                albums.add(album);
            }
        } catch (Exception e) {
            Utilities.showError(e);
        }
        return albums;
    }

    public static boolean register(String username,String password){
        try {
            PreparedStatement pst = DatabaseManager.getConnection().prepareStatement( "INSERT INTO projekt.user (username,password) VALUES (?,?)" );
            pst.setString(1,username );
            pst.setString(2,password );
            pst.execute();
            return true;
        } catch (Exception e) {
            Utilities.showError(e);
        }
        return false;
    }

    public static boolean checkLogin(String username, String password) {
        boolean check = false;
        try {
            CallableStatement cst = DatabaseManager.getConnection().prepareCall("{call projekt.checkLogin(?, ?)}");
            cst.setString(1, username);
            cst.setString(2, password);
            ResultSet rs = cst.executeQuery();
            check = rs.next();
        } catch (SQLException e) {
            Utilities.showError(e);
        }
        return check;
    }
    public static int getUserID(String username) {
        int id = -1; 
        try {
            PreparedStatement pst = getConnection().prepareStatement("SELECT id FROM projekt.user WHERE username = ?");
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

    public static void addRating(int itemID, int userID,int rating,char mode){
        try{
            CallableStatement cst = DatabaseManager.getConnection().prepareCall("{call projekt.addRating(?,?,?,?)}");
            cst.setInt(1, itemID);
            cst.setInt(2, userID);
            cst.setInt(3, rating);
            cst.setString(4,String.valueOf(mode));
            cst.execute();
        }catch(Exception e){
            Utilities.showError(e);
        }
    }
    public static Integer getRating(int userID, int itemID,char mode) {
        try {
            PreparedStatement pst;
            if(mode == 's'){
                pst = getConnection().prepareStatement("SELECT rating FROM projekt.songrating WHERE user_id = ? AND song_id = ?");
            }else if(mode == 'a'){
                pst = getConnection().prepareStatement("SELECT rating FROM projekt.albumrating WHERE user_id = ? AND album_id = ?");
            }else{ return -1;}
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
    public static Double getAverageRating(int itemID,char mode) {
        try {
            CallableStatement cst = getConnection().prepareCall("{call projekt.getAverageRating(?, ?)}");
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
    public static Pair<ArrayList<String>, ArrayList<Integer>> getUserPlaylists(int userID) {
        ArrayList<String> playlistNames = new ArrayList<>();
        ArrayList<Integer> playlistID = new ArrayList<>();
        try {
            PreparedStatement pst = getConnection().prepareStatement("SELECT * FROM projekt.getUserPlaylists(?)");
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
    public static int addPlaylist(String name){
        try {
            PreparedStatement pst = getConnection().prepareStatement( "INSERT INTO projekt.playlist (name,user_id) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, name);
            pst.setInt(2,TempData.getUserID());
            int rows = pst.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        }catch(Exception e){
            Utilities.showError(e);
        }
        return -1;
    }
    public static void addSongToPlaylist(int playlistID,int songID){
        try {
            PreparedStatement pst = getConnection().prepareStatement( "INSERT INTO projekt.playlistsong (playlist_id,song_id) VALUES (?,?)" );
            pst.setInt(1, playlistID);
            pst.setInt(2,songID);
            pst.execute();
        }catch(Exception e){
            Utilities.showError(e);
        }
    }
    public static Pair<ArrayList<String>,ArrayList<Integer>> getSongsFromPlaylist(int playlistID) {
        ArrayList<String> playlistNames = new ArrayList<>();
        ArrayList<Integer> songIDs = new ArrayList<>();
        try {
             PreparedStatement pst = getConnection().prepareStatement("SELECT * FROM projekt.getSongsFromPlaylist(?)");
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
    public static boolean addReview(String reviewText){
        try {
            PreparedStatement pst = DatabaseManager.getConnection().prepareStatement( "INSERT INTO projekt.review (song_id, user_id, review_text) VALUES (?, ?, ?)" );
            pst.setInt(1, TempData.getChosen());
            pst.setInt(2,TempData.getUserID());
            pst.setString(3, reviewText);
            int rows = pst.executeUpdate();

            return rows > 0;
        } catch (Exception e) {
            Utilities.showError(e);
        }
        return false;
    }
    public static ArrayList<Review> getReviews() {
        ArrayList<Review> reviews = new ArrayList<>();
        try{
             PreparedStatement pst = getConnection().prepareStatement("SELECT user_id, username, review_text FROM projekt.reviews where song_id = (?)");
             pst.setInt(1, TempData.getChosen());
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
    public static boolean followUser(int id) {
        try {
            int userID = TempData.getUserID();
            PreparedStatement pst = DatabaseManager.getConnection().prepareStatement("INSERT INTO projekt.follow (user_id, follows_id) VALUES (?, ?)");
            pst.setInt(1, userID);
            pst.setInt(2, id);
            int rows = pst.executeUpdate();

            return rows > 0;
        } catch (Exception e) {
            Utilities.showError(e);
            return false;
        }
    }
    public static boolean isFollowing(Integer userID,Integer visitUserID) {
        try {

            PreparedStatement pst = DatabaseManager.getConnection().prepareStatement("SELECT exists (select 1 from projekt.follow where user_id = ? and follows_id = ?)");

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
    public static Pair<ArrayList<String>, ArrayList<Integer>> getFollowingUsers(int userID) {
        ArrayList<String> usernames = new ArrayList<>();
        ArrayList<Integer> followsIDs = new ArrayList<>();
        try {
            CallableStatement cst = getConnection().prepareCall("{ call projekt.getFollowList(?) }");
            cst.setInt(1, userID);
            ResultSet rs = cst.executeQuery();

            while (rs.next()) {
                String username = rs.getString("username");
                int follows_id = rs.getInt("follows_id");

                usernames.add(username);
                followsIDs.add(follows_id);
            }
        } catch (SQLException e) {
            Utilities.showError(e);
        }

        return new Pair<>(usernames, followsIDs);
    }

}
