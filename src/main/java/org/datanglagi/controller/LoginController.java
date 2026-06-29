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
import org.datanglagi.DatabaseHalper;

public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;

    @FXML
    public void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        // 1. Validasi Input Kosong
        if (username.isEmpty() || password.isEmpty()) {
            tampilkanPesan("Username dan Password tidak boleh kosong, wak!");
            return;
        }

String query = "SELECT username, email, durasi_haid FROM user WHERE username = ? AND password = ?";

try (Connection conn = DatabaseHalper.getConnection();
     PreparedStatement stmt = conn.prepareStatement(query)) {
    
    stmt.setString(1, username);
    stmt.setString(2, password);
    
    try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
            
            String usernameLogin = rs.getString("username");
            String emailLogin = rs.getString("email");
            int durasiStr = rs.getInt("durasi_haid"); 

            UserSession.getInstance().startSession(usernameLogin, emailLogin, durasiStr);

            try {
                App.setRoot("navbar");
            } catch (IOException e) {
                e.printStackTrace();
                tampilkanPesan("Gagal memuat halaman aplikasi.");
            }
        } else {
            tampilkanPesan("Username atau Password salah, wak! Silakan cek kembali.");
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
            tampilkanPesan("Gagal membuka halaman daftar.");
        }
    }

    @FXML
    public void handleLupaPassword() {
        try {
            // Mengalihkan panggung besar ke file fxml lupa password kamu
            App.setRoot("password"); 
        } catch (IOException e) {
            e.printStackTrace();
            tampilkanPesan("Gagal membuka halaman lupa password, wak!");
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