package org.datanglagi.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.datanglagi.DatabaseHalper;
import org.datanglagi.ViewLoader; // Asumsi kamu punya class untuk ganti scene

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PasswordController {

    @FXML private TextField txtUsername;
    @FXML private TextField txtEmail;

    @FXML
    private void handleDapatkan() {
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
                
                // Munculkan dialog informasi akun
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informasi Akun");
                alert.setHeaderText("Akun Ditemukan!");
                alert.setContentText("Password akun kamu adalah: " + passwordDitemukan);
                
                // Setelah user klik "OK" pada dialog
                alert.showAndWait();
                
                // Arahkan ke halaman Login
                ViewLoader.loadView("login.fxml", (Stage) txtUsername.getScene().getWindow());
                
            } else {
                tampilkanAlert(Alert.AlertType.ERROR, "Gagal", "Username atau Email tidak ditemukan.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            tampilkanAlert(Alert.AlertType.ERROR, "Error", "Terjadi kesalahan koneksi database.");
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
