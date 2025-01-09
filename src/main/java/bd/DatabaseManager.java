package bd;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
        if (!table.equals(artistTable) && !table.equals(songTable) && !table.equals(albumTable)) {
            throw new Exception("Tablica " + table + " nie istnieje");
        }
        try { 
            PreparedStatement pst = connection.prepareStatement("SELECT id FROM projekt." + table + " where nazwa=? ",ResultSet.CONCUR_UPDATABLE);
            pst.setString(1,name);
            ResultSet rs = pst.executeQuery();
            return rs.next();
            }
            catch(SQLException e)  {
                System.out.println(" Blad podczas przetwarzania danych:"+e); 
                return false;  
            }
        }

    public static void insertSong(){
        try { 
            PreparedStatement pstSong = getConnection().prepareStatement( "INSERT INTO projekt.piosenka (nazwa,napisana) VALUES (?,?)" );
            pstSong.setString(1, TempData.getSongName());
            pstSong.setInt(2, TempData.getSongYear());
            int rows = pstSong.executeUpdate();
            System.out.println("Polecenie -  INSERT Song - ilosc dodanych rekordow: " + String.valueOf(rows));
            insertSongExtraTable(getSongLastID(TempData.getSongName()),getArtistID(TempData.getArtistName()));
        }catch(Exception e){
            Utilities.showError(e);
        }
    }

    public static void insertSongExtraTable(int songID, int artistID){
        try { 
            PreparedStatement pst = getConnection().prepareStatement( "INSERT INTO projekt.artystapiosenka (piosenka_id,artysta_id) VALUES (?,?)" );
            pst.setInt(1, songID);
            pst.setInt(2, artistID);
            int rows = pst.executeUpdate();
            System.out.println("Polecenie -  INSERT SongArtistTable - ilosc dodanych rekordow: " + String.valueOf(rows));
            if(TempData.getAlbumName() != null){
                insertAlbum(songID, artistID);
            }
        }catch(Exception e){
            Utilities.showError(e);
        }
    }

    public static void insertAlbum(int songID, int artistID){
        try { 
            if(InDatabase(TempData.getAlbumName(), albumTable)){
                insertAlbumExtraTable(songID, getAlbumID(TempData.getAlbumName(), artistID));
                return;
            }
            PreparedStatement pst = getConnection().prepareStatement( "INSERT INTO projekt.plyta (artysta_id,nazwa,napisana,okladka) VALUES (?,?,?,?)" );
            pst.setInt(1, artistID);
            pst.setString(2, TempData.getAlbumName());
            pst.setInt(3, TempData.getSongYear()); // Change with different implementation
            
            File tempFile = TempData.getFile();
            if (tempFile != null) {
            FileInputStream fis = new FileInputStream(tempFile);
            pst.setBinaryStream(4, fis, (int) tempFile.length());
            } else {
                pst.setNull(4, java.sql.Types.BINARY);
            }

            int rows = pst.executeUpdate();
            System.out.println("Polecenie -  INSERT Album - ilosc dodanych rekordow: " + String.valueOf(rows));
            insertAlbumExtraTable(songID, getAlbumID(TempData.getAlbumName(), artistID));
        }catch(Exception e){
            Utilities.showError(e);
        }
    }

    public static void insertAlbumExtraTable(int songID,int albumID){
        try {
            PreparedStatement pst = getConnection().prepareStatement( "INSERT INTO projekt.piosenkaplyta (piosenka_id,plyta_id) VALUES (?,?)" );
            pst.setInt(1, songID);
            pst.setInt(2, albumID);
            int rows = pst.executeUpdate();
            System.out.println("Polecenie -  INSERT AlbumExtraTable - ilosc dodanych rekordow: " + String.valueOf(rows));
        } catch (Exception e) {
            Utilities.showError(e);
        }
    }

    public static void insertArtist(String name, Integer start,Integer end){
        try { 
            PreparedStatement pstArtist = DatabaseManager.getConnection().prepareStatement( "INSERT INTO projekt.artysta (nazwa,poczatek_kariery,koniec_kariery) VALUES (?,?,?)" );
            pstArtist.setString(1, name);
            pstArtist.setInt(2, start);
            // pstArtist.setInt(3,end);
            if(end != null){
                pstArtist.setInt(3,end);
            }else{
                pstArtist.setNull(3, java.sql.Types.NULL);
            }
            int rows = pstArtist.executeUpdate();
            System.out.println("Polecenie -  INSERT Artist - ilosc dodanych rekordow: " + String.valueOf(rows));
        }catch(Exception e){
            Utilities.showError(e);
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

    public static void populateGrid(int id,ArrayList<Label> songNameList,ArrayList<Label> albumNameList,ArrayList<Label> artistNameList,ArrayList<ImageView> imageList){
        int size = -1;
        try {
            CallableStatement cst = getConnection().prepareCall( "{call projekt.getAllSongInfo(?)}" );
            cst.setInt(1, id);  
            ResultSet rs ;
            rs = cst.executeQuery();
            byte[] byteArray = null;
            if(rs.next()){
                songNameList.add(new Label(rs.getString("nazwa_piosenki")));
                albumNameList.add(new Label(rs.getString("nazwa_plyty")));
                artistNameList.add(new Label(rs.getString("nazwa_artysty")));
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
    }

}

 