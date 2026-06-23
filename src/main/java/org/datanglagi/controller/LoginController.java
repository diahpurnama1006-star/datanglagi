package org.datanglagi.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.datanglagi.App;
import org.datanglagi.UserSession;
import config.DatabaseHalper;

public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;

    @FXML
    public void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            tampilkanPesan("Username dan Password tidak boleh kosong, wak!");
            return;
        }

        String query = "SELECT id_user, username FROM user WHERE username = ?";

        try (Connection conn = DatabaseHalper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int idUserLogin = rs.getInt("id_user");
                    String usernameLogin = rs.getString("username");

                    // Set Sesi Aktif agar halaman utama bisa tahu siapa yang login
                    UserSession.setSession(idUserLogin, usernameLogin);

                    // Pindah ke halaman navbar utama
                    try {
                        App.setRoot("navbar");
                    } catch (IOException e) {
                        e.printStackTrace();
                        tampilkanPesan("Gagal memuat halaman aplikasi.");
                    }
                } else {
                    tampilkanPesan("Username tidak ditemukan. Silakan daftar dulu, wak!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            tampilkanPesan("Terjadi kesalahan database: " + e.getMessage());
        }
    }

    @FXML
    public void handleBukaSignup() {
        try {
            App.setRoot("signup");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLupaPassword() {
        tampilkanPesan("Fitur lupa password sedang dalam pengembangan.");
    }

    private void tampilkanPesan(String pesan) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Informasi");
        a.setHeaderText(null);
        a.setContentText(pesan);
        a.showAndWait();
    }
}