package bd;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;

public class ReadJsonData {

    public static void fun() {

        String filePath = "expanded_data.json";
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray jsonArray = new JSONArray(content);
            
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                File img = new File(jsonObject.getString("groupImagePath"));
                Integer v = Integer.valueOf(jsonObject.getString("written"));
                Integer start = Integer.valueOf(jsonObject.getString("career_start"));
                Integer end =  null;

                BufferedImage image = Scalr.resize(ImageIO.read(img), 200,150);
                File resizedFile = new File("temp.jpg");


                ImageIO.write(image, "jpg", resizedFile);
                img = resizedFile;

                if(jsonObject.getString("career_end") != ""){
                     end = Integer.valueOf(jsonObject.getString("career_end"));
                }

                
                TempData.setData(jsonObject.getString("title"),jsonObject.getString("artist"),jsonObject.getString("album"),img,v,v) ;

                try {
                    if(!DatabaseManager.InDatabase(jsonObject.getString("artist"),"artysta")){
                        System.out.println("Adding artist no " + String.valueOf(i));
                        DatabaseManager.insertArtist(jsonObject.getString("artist"), start, end);
                        DatabaseManager.insertSong();
                        TempData.clear();
                    }else{
                        System.out.println("Inserting song no " + String.valueOf(i));
                        DatabaseManager.insertSong();
                        TempData.clear();
                    } 

                } catch (Exception e) {
                    Utilities.showError(e);
                }
            }
        } catch (IOException e) {
            Utilities.showError(e);
        }
    }
}
