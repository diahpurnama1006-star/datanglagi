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
public void handleLupaPassword() {
    try {
        // Ganti "password" dengan nama file fxml halaman lupa password kamu
        // Contoh: jika filenya adalah "lupapassword.fxml", maka gunakan "lupapassword"
        App.setRoot("password"); 
    } catch (IOException e) {
        e.printStackTrace();
        tampilkanPesan("Gagal membuka halaman lupa password, wak!");
    }
}

    @FXML
    public void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            tampilkanPesan("Username dan Password tidak boleh kosong, wak!");
            return;
        }

        // Query untuk memverifikasi user dan menarik data siklus terbaru
        String query = "SELECT u.username, u.email, s.durasi_haid, s.panjang_siklus " +
                       "FROM user u " +
                       "LEFT JOIN siklus_haid s ON u.username = s.username " +
                       "WHERE u.username = ? AND u.password = ? " +
                       "ORDER BY s.id_siklus DESC LIMIT 1";

        try (Connection conn = DatabaseHalper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Berhasil login, simpan ke sesi
                    String user = rs.getString("username");
                    String email = rs.getString("email");
                    int durasi = rs.getInt("durasi_haid");
                    int panjang = rs.getInt("panjang_siklus");

                    UserSession.getInstance().startSession(user, email, durasi, panjang);
                    
                    App.setRoot("navbar"); // Pindah ke menu utama
                } else {
                    tampilkanPesan("Username atau Password salah, wak!");
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            tampilkanPesan("Terjadi kesalahan: " + e.getMessage());
        }
    }

    @FXML
    public void handleBukaSignup() throws IOException {
        App.setRoot("signup");
    }

    private void tampilkanPesan(String pesan) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Informasi");
        a.setHeaderText(null);
        a.setContentText(pesan);
        a.showAndWait();
    }
}