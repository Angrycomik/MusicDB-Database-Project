package bd;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class InsertController {

    @FXML
    private Button addBtn;

    @FXML
    private TextField artistName;

    @FXML
    private RadioButton albumRadio;

    @FXML
    private RadioButton singleRadio;

    @FXML
    private VBox rootVBox;

    @FXML
    private TextField songName;
    
    @FXML
    private ToggleGroup songType;

    @FXML
    private TextField year;

    @FXML
    private TextField albumTextField;

    @FXML
    public void initialize() {
        
        year.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                year.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        songType.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == albumRadio) {
                addAlbumTextField();
            } else if (newValue == singleRadio) {
                removeAlbumTextField();
            }
        });
    }

    private void addAlbumTextField() {
        if (albumTextField == null) {
            albumTextField = new TextField();
            albumTextField.setPromptText("Nazwa płyty");
            rootVBox.getChildren().add(rootVBox.getChildren().size() - 1, albumTextField);
        }
    }

    private void removeAlbumTextField() {
        if (albumTextField != null) {
            rootVBox.getChildren().remove(albumTextField);
            albumTextField = null;
        }
    }


    @FXML
    void onAddClick(ActionEvent event) throws IOException {
        if(artistName.getText().trim().isEmpty() || songName.getText().trim().isEmpty()){
            return;
        }
        
        int yearInt = Integer.parseInt(year.getText());
        TempData.setData(songName.getText(),artistName.getText(),yearInt) ;
        if( Utilities.checkYear(yearInt)){
            try {
                if(!DatabaseManager.InDatabase(artistName.getText(),"artysta")){
                    Utilities.showNotFound(artistName.getText());
                    App.setRoot("insertartist");
                }else{
                    DatabaseManager.insertSong();
                    TempData.clear();
                    App.setRoot("mainscene");
                } 
            } catch (Exception e) {
                Utilities.showError(e);
            }

        }
    }

}

// try { 
//     //  Wstawianie rekordow do bazy danych
//        //  Wykorzystanie metody executeUpdate()  
//        PreparedStatement pst = c.prepareStatement( "INSERT INTO lab11.kursline (kurs_id,wykladowca_id) VALUES (?,?)" );
//        pst.setInt(1,k_index);
//        pst.setInt(2,w_index);
//         int rows ;
//        rows = pst.executeUpdate();
//        System.out.print("Polecenie 2 -  INSERT - ilosc dodanych rekordow: ") ;
//        System.out.println(rows) ;
          
//          }
//        catch(SQLException e)  {
//            System.out.println("Blad podczas przetwarzania danych:"+e) ;   }  