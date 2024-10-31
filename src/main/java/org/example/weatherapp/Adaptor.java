package org.example.weatherapp;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.scene.control.Alert;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Adaptor {

    private ObjectMapper objectMapper;

    public Adaptor() {
        objectMapper = new ObjectMapper();
    }

    private static void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void serializeToJson(List<String> favoriteCities) {
        try
        {
            if (favoriteCities.size() == 1) {
                throw new CustomException("Favorite cities list is empty");
            }

            File file = new File("favorite_cities.json");
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file);
            objectMapper.writeValue(fileWriter, favoriteCities);
            fileWriter.close();
            showAlert(Alert.AlertType.INFORMATION, "File Saved", null, "The file has been saved successfully.");
        }
        catch (IOException e) {
            e.printStackTrace(); // serialization error
        }
        catch (CustomException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Favorites list is empty", "Please add cities to the favorites list before saving it.");
        }
    }

    public List<String> deserializeFromJson(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                throw new CustomException("File not found: " + filename);
            }
            showAlert(Alert.AlertType.INFORMATION, "Import succesful", null, "Favorites list updated.");
            return objectMapper.readValue(file, new TypeReference<List<String>>() {});
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (CustomException e) {
            showAlert(Alert.AlertType.ERROR, "Error", null, e.getMessage());
        }
        return null;
    }
}
