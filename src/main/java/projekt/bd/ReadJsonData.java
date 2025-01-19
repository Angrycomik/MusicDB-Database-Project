package projekt.bd;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReadJsonData {

    public static void fun() {

        String filePath = "data.json";
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonArray = objectMapper.readTree(content);

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonNode jsonObject = jsonArray.get(i);
                File img = new File(jsonObject.get("groupImagePath").asText());
                Integer v = jsonObject.get("written").asInt();
                Integer start = jsonObject.get("career_start").asInt();
                Integer end = null;

                BufferedImage image = Scalr.resize(ImageIO.read(img), 200,150);
                File resizedFile = new File("temp.jpg");


                ImageIO.write(image, "jpg", resizedFile);
                img = resizedFile;

                if (!jsonObject.get("career_end").asText().isEmpty()) {
                    end = jsonObject.get("career_end").asInt();
                }


                TempData.setData(jsonObject.get("title").asText(), jsonObject.get("artist").asText(), jsonObject.get("album").asText(), img, v, v);

                try {
                    if (!DatabaseManager.InDatabase(jsonObject.get("artist").asText(), "artysta")) {
                        System.out.println("Adding artist no " + String.valueOf(i));
                        DatabaseManager.insertArtist(jsonObject.get("artist").asText(), start, end,v);
                        DatabaseManager.insertSong(jsonObject.get("title").asText(), jsonObject.get("artist").asText(), v);
                        DatabaseManager.insertAlbum(jsonObject.get("title").asText(), jsonObject.get("artist").asText(), DatabaseManager.getArtistID(jsonObject.get("artist").asText()), jsonObject.get("album").asText(), v, jsonObject.get("run_order").asInt());
                        TempData.clear();
                    } else {
                        System.out.println("Inserting song no " + String.valueOf(i));
                        DatabaseManager.insertSong(jsonObject.get("title").asText(), jsonObject.get("artist").asText(), v);
                        DatabaseManager.insertAlbum(jsonObject.get("title").asText(), jsonObject.get("artist").asText(), DatabaseManager.getArtistID(jsonObject.get("artist").asText()), jsonObject.get("album").asText(), v, jsonObject.get("run_order").asInt());
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
