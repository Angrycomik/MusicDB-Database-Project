package projekt.bd;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

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
            PreparedStatement pst = connection.prepareStatement("SELECT id FROM projekt.plyta where nazwa=? and artysta_id=? ",ResultSet.CONCUR_UPDATABLE);
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
            PreparedStatement pst = connection.prepareStatement("SELECT id FROM projekt.artysta where nazwa=? ",ResultSet.CONCUR_UPDATABLE);
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

    public static int getSongLastID(String name){
        try { 
            PreparedStatement pst = connection.prepareStatement("SELECT id FROM projekt.piosenka where nazwa=? ORDER BY id DESC LIMIT 1",ResultSet.CONCUR_UPDATABLE);
            pst.setString(1, name);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
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
                System.out.println(" Blad podczas przetwarzania danych:"+e); 
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
            // stmt.execute("RESET application_name");
        }catch(Exception e){
            System.out.println(" Blad podczas przetwarzania danych:"+e); 
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
            PreparedStatement stmt = getConnection().prepareStatement("SET tempvar.song_name = '" + songName + "'");
            stmt.execute();
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

    public static boolean insertArtist(String name, Integer start,Integer end){
        try { 
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
            System.out.println(" Blad podczas przetwarzania danych:"+e); 
            Utilities.showError(e);
            return false;
        }
    }
    
    public static int getDBLenght(){
        int size = 0;
        try {
            CallableStatement cst = getConnection().prepareCall( "{call projekt.getItemCount()}" );  
            ResultSet rs ;
            rs = cst.executeQuery();
            if(rs.next()){
                size = rs.getInt(1);
            }
            rs.close();      
            cst.close();
        } catch (Exception e) {
            Utilities.showError(e);
        }
        return size;
    }

    public static VBox populateGrid(int id,ArrayList<ImageView> imageList){
        int size = -1;
        VBox vbox = new VBox();
        try {
            CallableStatement cst = getConnection().prepareCall( "{call projekt.getSongInfo(?)}" );
            cst.setInt(1, id);  
            ResultSet rs ;
            rs = cst.executeQuery();
            byte[] byteArray = null;
            if(rs.next()){
                vbox.setSpacing(5); 
                vbox.setAlignment(Pos.CENTER); 
                Label songLabel = new Label(rs.getString("nazwa_piosenki"));
                Utilities.turnLabelClickable(songLabel,id,"songpage","d");

                Label albumLabel = new Label(rs.getString("nazwa_plyty"));
                Utilities.turnLabelClickable(albumLabel,id,"albumpage","d");

                Label artistLabel = new Label(rs.getString("nazwa_artysty"));
                Utilities.turnLabelClickable(artistLabel,id,"artistpage","d");

                vbox.getChildren().addAll(songLabel, albumLabel, artistLabel);

                byteArray = rs.getBytes("okladka");
            }
            Image img;
            if(byteArray == null){
                 img = new Image(new FileInputStream("covers\\logo.png"));
            }else{
                img = new Image(new ByteArrayInputStream(byteArray));
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
            if(rs.next()){
                song = new Song(rs.getString("nazwa_piosenki"), rs.getString("nazwa_artysty"), rs.getString("nazwa_plyty"), rs.getInt("rok_napisania"),rs.getInt("artysta_piosenka_id"));
                byteArray = rs.getBytes("okladka");
            }
            Image img;
            if(byteArray == null){
                 img = new Image(new FileInputStream("covers\\logo.png"));
            }else{
                img = new Image(new ByteArrayInputStream(byteArray));
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
            byte[] byteArray = null;
            ArrayList<String> songList = null;

            if (rs.next()) {
                byteArray = rs.getBytes("okladka");

                Array songs = rs.getArray("songs");
                if (songs != null) {
                    String[] songsString = (String[]) songs.getArray();
                    songList = new ArrayList<>(Arrays.asList(songsString));
                }
                Image img;
                if (byteArray == null) {
                    img = new Image(new FileInputStream("covers\\logo.png"));
                } else {
                    img = new Image(new ByteArrayInputStream(byteArray));
                }
                album = new Album(rs.getString("nazwa_plyty"), rs.getString("nazwa_artysty"), rs.getInt("rok_napisania"),rs.getInt("album_id"), img, songList);
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
            ArrayList<String> albumList = null;
            if (rs.next()) {
                Array songs = rs.getArray("songs");
                if (songs != null) {
                    String[] songsString = (String[]) songs.getArray();
                    songList = new ArrayList<>(Arrays.asList(songsString));
                }
    
                Array albumsArray = rs.getArray("albums");
                if (albumsArray != null) {
                    String[] albums = (String[]) albumsArray.getArray();
                    albumList = new ArrayList<>(Arrays.asList(albums));
                }
    
                artist = new Artist(rs.getString("nazwa_artysty"), rs.getInt("poczatek_kariery"), rs.getInt("koniec_kariery"),rs.getInt("song_count"), songList, albumList);
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
            while (rs.next() && songs.size()<50) {
                String songName = rs.getString("nazwa_piosenki");
                String artistName = rs.getString("nazwa_artysty");
                String albumName = rs.getString("nazwa_plyty");
                Integer year = rs.getInt("rok_napisania");

                Song song = new Song(songName, artistName, albumName, year);
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
            
            while (rs.next() && artists.size()<50) {
                String artistName = rs.getString("nazwa_artysty");
                Integer startYear = rs.getInt("poczatek_kariery");
                Integer endYear = rs.getInt("koniec_kariery");
    
                Artist artist = new Artist(artistName, startYear, endYear);
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
                // byte[] cover = rs.getBytes("okladka");
    
                Album album = new Album(albumName, artistName, releaseYear);
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

    public static void addRating(int songID, int userID,int rating){
        try{
            CallableStatement cst = DatabaseManager.getConnection().prepareCall("{call projekt.addSongRating(?,?,?)}");
            cst.setInt(1, songID);
            cst.setInt(2, userID);
            cst.setInt(3, rating);
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
                pst = getConnection().prepareStatement("SELECT rating FROM projekt.albumrating WHERE user_id = ? AND plyta_id = ?");
            }else{ return -1;}
            pst.setInt(1, userID);
            pst.setInt(2, itemID);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt("rating");  
            } 
        } catch (SQLException e) {
            System.out.println(e);
            Utilities.showError(e); 
        }
        return null; 
    }
     
}
