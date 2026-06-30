package org.datanglagi.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.datanglagi.App;
import org.datanglagi.DatabaseHalper;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PasswordController {

    @FXML private TextField txtUsername;
    @FXML private TextField txtEmail;

    // Perhatikan: Menambahkan (MouseEvent event) agar sinkron dengan onMouseClicked
    @FXML
    private void handleDapatkan(MouseEvent event) {
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();

        if (username.isEmpty() || email.isEmpty()) {
            tampilkanAlert(Alert.AlertType.WARNING, "Peringatan", "Username dan Email harus diisi!");
            return;
        }

        String query = "SELECT password FROM user WHERE username = ? AND email = ?";

        try (Connection conn = DatabaseHalper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            stmt.setString(2, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String passwordDitemukan = rs.getString("password");
                tampilkanAlert(Alert.AlertType.INFORMATION, "Informasi Akun", "Password kamu adalah: " + passwordDitemukan);
                App.setRoot("login");
            } else {
                tampilkanAlert(Alert.AlertType.ERROR, "Gagal", "Username atau Email tidak ditemukan.");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            tampilkanAlert(Alert.AlertType.ERROR, "Error", "Database error.");
        }
    }
    
    @FXML
    private void handleKembali(MouseEvent event) {
        try {
            App.setRoot("login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tampilkanAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}