package org.datanglagi;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ViewLoader {
    public static void loadView(String fxmlFile, Stage stage) {
        try {
            // Memuat file FXML dari folder resources
            FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource("/org/datanglagi/fxml/" + fxmlFile));
            Parent root = loader.load();
            
            // Menampilkan scene baru ke stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Gagal memuat halaman: " + fxmlFile);
        }
    }
}