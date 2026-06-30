package org.datanglagi.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.datanglagi.App;
import org.datanglagi.DatabaseHalper;
import org.datanglagi.UserSession;
import javafx.scene.input.MouseEvent;


public class SignupController {

    @FXML private TextField txtUsernameSignUp; 
    @FXML private TextField txtEmailSignUp;
    @FXML private PasswordField txtPasswordSignUp;
    @FXML private PasswordField txtKonfirmasiSignUp;
    @FXML private TextField txtDurasiHaid;
    @FXML private TextField txtPanjangSiklus; // Tambahkan ini

@FXML
public void handleDaftar() {
    String username = txtUsernameSignUp.getText().trim();
    String email = txtEmailSignUp.getText().trim();
    String password = txtPasswordSignUp.getText().trim();
    int durasiHaid = Integer.parseInt(txtDurasiHaid.getText().trim());
    int panjangSiklus = Integer.parseInt(txtPanjangSiklus.getText().trim());
    
    // 1. Query Tabel User (3 kolom)
    String queryUser = "INSERT INTO user (username, email, password) VALUES (?, ?, ?)";
    // 2. Query Tabel Siklus (3 kolom: username, panjang, durasi)
    String queryHaid = "INSERT INTO siklus_haid (username, panjang_siklus, durasi_haid) VALUES (?, ?, ?)";

    try (Connection conn = DatabaseHalper.getConnection()) {
        conn.setAutoCommit(false); // Transaksi dimulai

        // Isi Tabel User
        try (PreparedStatement stmtUser = conn.prepareStatement(queryUser)) {
            stmtUser.setString(1, username);
            stmtUser.setString(2, email);
            stmtUser.setString(3, password);
            stmtUser.executeUpdate();
        }

        // Isi Tabel Siklus
        try (PreparedStatement stmtHaid = conn.prepareStatement(queryHaid)) {
            stmtHaid.setString(1, username); // Menggunakan username yang sama
            stmtHaid.setInt(2, panjangSiklus);
            stmtHaid.setInt(3, durasiHaid);
            stmtHaid.executeUpdate();
        }

        conn.commit(); // Simpan kedua tabel
        tampilkanPesan("Pendaftaran berhasil!");
        App.setRoot("navbar");

    } catch (Exception e) {
        e.printStackTrace();
        tampilkanPesan("Gagal: " + e.getMessage());
    }
}
    @FXML
    public void handleMasuk() {
        try {
            App.setRoot("login");
        } catch (IOException e) {
            e.printStackTrace();
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