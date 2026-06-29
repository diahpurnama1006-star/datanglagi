package org.datanglagi.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.datanglagi.App; 

public class NavbarController {
    @FXML private StackPane contentArea; // Area untuk menampilkan halaman

    // Buat metode static agar mudah dipanggil dari mana saja
    private static StackPane staticContentArea;
    
        @FXML
    private void keBeranda(MouseEvent event) {
        loadPage("homepage");
    }

    @FXML
    private void keKalender(MouseEvent event) {
        loadPage("calender"); 
    }

    @FXML
    private void keInputHarian(MouseEvent event) {
        loadPage("inputharian");
    }

    @FXML
    private void kePerawatan(MouseEvent event) {
        loadPage("perawatan");
    }

    @FXML
    private void keAkun(MouseEvent event) {
        loadPage("user"); 
    }

    @FXML
    public void initialize() {
        staticContentArea = contentArea;
        // Load default page (Homepage)
        loadPage("homepage");
    }

public static void loadPage(String fxml) {
    try {
        String path = "/org/datanglagi/fxml/" + fxml + ".fxml";
        Parent root = FXMLLoader.load(NavbarController.class.getResource(path));
        
        if (staticContentArea != null) {
            staticContentArea.getChildren().clear();
            staticContentArea.getChildren().add(root);
        } else {
            System.out.println("ERROR: staticContentArea masih NULL!");
        }
    } catch (IOException e) {
        e.printStackTrace();
        System.out.println("Gagal memuat halaman: " + fxml);
    }
}
}