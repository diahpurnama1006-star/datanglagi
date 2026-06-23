package org.datanglagi.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Node;
import org.datanglagi.App;
import org.datanglagi.UserSession;
import java.io.IOException;

public class UserController {

    @FXML private Label labelUsername; // Sesuai fx:id label tempat namamu di Scene Builder

@FXML
    public void initialize() {
        // Ambil nama user aktif dari session saat login/signup
        String namaAktif = UserSession.getUsername();
        
        // PROTEKSI AMAN: Cek dulu apakah labelUsername sudah terhubung dengan FXML
        if (labelUsername != null) {
            if (namaAktif != null && !namaAktif.isEmpty()) {
                labelUsername.setText(namaAktif);
            } else {
                labelUsername.setText("Pengguna");
            }
        } else {
            // Jika masuk ke sini, berarti fx:id di Scene Builder belum ditempel dengan benar
            System.out.println("Peringatan: Komponen labelUsername masih bernilai null!");
        }
    }

    @FXML
    private void handleLogout(MouseEvent event) throws IOException {
        // 1. Reset data sesi di memori
        UserSession.clearSession();
        
        // 2. Lempar balik ke halaman login utama
        App.setRoot("login");
    }

    // --- FXML METHOD NAVIGASI KAKAK DI BAWAH ---
    private void pindahHalaman(MouseEvent event, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}