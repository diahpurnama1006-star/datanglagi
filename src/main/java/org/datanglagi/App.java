package org.datanglagi;

import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // Pastikan nama file "login.fxml" ada di folder resources yang tepat
        scene = new Scene(loadFXML("loading"), 360, 640);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
    // Sesuai struktur folder Anda: /org/datanglagi/fxml/
    String path = "/org/datanglagi/fxml/" + fxml + ".fxml"; 
    
    System.out.println("Mencoba memuat FXML dari: " + path);
    
    URL resourceUrl = App.class.getResource(path);
    
    // Pengecekan jika file tidak ditemukan
    if (resourceUrl == null) {
        throw new IOException("File FXML tidak ditemukan di: " + path);
    }
    
    // Membuat loader dengan URL yang sudah valid
    FXMLLoader fxmlLoader = new FXMLLoader(resourceUrl);
    return fxmlLoader.load();
}

    public static void main(String[] args) {
        launch(args);
    }
}