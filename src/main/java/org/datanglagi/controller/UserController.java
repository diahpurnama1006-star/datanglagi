package org.datanglagi.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.datanglagi.App;
import org.datanglagi.UserSession;
import java.io.IOException;

public class UserController {

    @FXML private Label lblUsername; 

    @FXML
    public void initialize() {
        // Menggunakan instance dari session untuk mengambil data
        UserSession session = UserSession.getInstance();
        String namaAktif = session.getUsername();
        
        if (lblUsername != null) {
            if (namaAktif != null && !namaAktif.isEmpty()) {
                lblUsername.setText("Selamat Datang, " + namaAktif + "!");
            } else {
                lblUsername.setText("Selamat Datang, Teman!");
            }
        }
    }

    @FXML
    private void handleTentang(MouseEvent event) {
        tampilkanPesan("DatangLagi adalah aplikasi pelacak siklus menstruasi dan kesehatan wanita modern.");
    }

    @FXML
    private void handleLogout(MouseEvent event) {
        // PENTING: Pastikan method clearSession() ada di UserSession.java
        UserSession.getInstance().clearSession();
        
        try {
            App.setRoot("login");
        } catch (IOException e) {
            e.printStackTrace();
            tampilkanPesan("Gagal keluar, silakan coba lagi.");
        }
    }

    private void tampilkanPesan(String pesan) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Informasi");
        a.setHeaderText(null);
        a.setContentText(pesan);
        a.showAndWait();
    }
}