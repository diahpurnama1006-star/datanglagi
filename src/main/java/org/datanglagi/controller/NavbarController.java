package org.datanglagi.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.datanglagi.App; // Pastikan class App di-import!

public class NavbarController {

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        // Otomatis memuat homepage saat pertama kali login berhasil
        loadPage("homepage"); 
    }

    @FXML
    private void keBeranda(MouseEvent event) {
        loadPage("homepage");
    }

    @FXML
    private void keKalender(MouseEvent event) {
        // Sesuaikan nama file fxml kalender kamu (gunakan huruf kecil/besar yang sama persis dengan nama filenya)
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

    // METHOD FIX: Menyelaraskan load resource dengan sistem class App kamu
private void loadPage(String fxmlFileName) {
    try {
        // MENYESUAIKAN DENGAN STRUKTUR PROJECT KAMU:
        // Menambahkan "/fxml/" sesuai dengan letak file kamu di screenshot
        String path = "/org/datanglagi/fxml/" + fxmlFileName + ".fxml";
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
        Parent node = fxmlLoader.load();
        
        // Bersihkan konten center lama, lalu masukkan halaman baru
        contentArea.getChildren().clear();
        contentArea.getChildren().add(node);
        
    } catch (IOException e) {
        System.out.println("[ERROR] Gagal memuat file FXML di navbar: " + fxmlFileName);
        e.printStackTrace();
    } catch (NullPointerException npe) {
        System.out.println("[ERROR] Jalur salah! File tidak ditemukan di /org/datanglagi/fxml/" + fxmlFileName + ".fxml");
        npe.printStackTrace();
    }
}
}