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
import java.time.LocalDate;
import org.datanglagi.App;
import config.DatabaseHalper;

public class SignupController {

    // Menambahkan field username sesuai fx:id di FXML baru kamu
    @FXML private TextField txtusername; 
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtKonfirmasi;
    @FXML private TextField txtDurasiHaid;

    @FXML
    public void handleDaftar() {
        String username = txtusername.getText().trim();
        String email = txtEmail.getText().trim();
        String pass = txtPassword.getText().trim();
        String konf = txtKonfirmasi.getText().trim();
        String durasiStr = txtDurasiHaid.getText().trim();

        // 1. Validasi Input Kosong (ditambah cek username)
        if (username.isEmpty() || email.isEmpty() || pass.isEmpty() || konf.isEmpty() || durasiStr.isEmpty()) {
            tampilkanPesan("Data tidak lengkap. Mohon lengkapi seluruh kolom, wak!");
            return;
        } 
        
        if (pass.length() < 8) {
            tampilkanPesan("Kata sandi minimal harus terdiri dari 8 karakter.");
            return;
        }
        
        if (!pass.equals(konf)) {
            tampilkanPesan("Konfirmasi kata sandi tidak sesuai.");
            return;
        }

        // Validasi format angka untuk durasi haid
        int durasiHaid = 0;
        try {
            durasiHaid = Integer.parseInt(durasiStr);
        } catch (NumberFormatException e) {
            tampilkanPesan("Durasi haid harus berupa angka saja (contoh: 4).");
            return;
        }

        // 2. Simpan Data ke Database MySQL
        // Kolom username diisi oleh nilai dari inputan txtusername
        String queryUser = "INSERT INTO user (username) VALUES (?)";
String queryHaid = "INSERT INTO riwayat_haid (id_user, tanggal_mulai, durasi_haid) VALUES (?, ?, ?)";

// TRIK JITU: Buat variabel penampung di luar block try agar bisa dibaca sampai bawah
int idUserBaruTerdaftar = 0; 

try (Connection conn = DatabaseHalper.getConnection()) {
    conn.setAutoCommit(false); // Mulai transaksi database

    try {
        // A. Masukkan username baru ke tabel user
        try (PreparedStatement stmtUser = conn.prepareStatement(queryUser, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            stmtUser.setString(1, username); 
            stmtUser.executeUpdate();

            // Mengambil id_user auto-increment yang dihasilkan oleh MySQL
            try (ResultSet generatedKeys = stmtUser.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // Isi variabel penampung yang kita buat di luar tadi
                    idUserBaruTerdaftar = generatedKeys.getInt(1);

                    // B. Masukkan data awal durasi haid ke tabel riwayat_haid
                    try (PreparedStatement stmtHaid = conn.prepareStatement(queryHaid)) {
                        stmtHaid.setInt(1, idUserBaruTerdaftar);
                        stmtHaid.setDate(2, java.sql.Date.valueOf(LocalDate.now())); 
                        stmtHaid.setInt(3, durasiHaid);
                        stmtHaid.executeUpdate();
                    }
                } else {
                    throw new SQLException("Gagal mendapatkan ID User baru.");
                }
            }
        }

        // Komit semua query jika tidak ada kendala
        conn.commit();
        
        // SEKARANG AMAN DI SINI, WAK! Panggil session-nya tepat setelah commit
        org.datanglagi.UserSession.setSession(idUserBaruTerdaftar, username);

    } catch (SQLException e) {
        conn.rollback(); // Batalkan jika ada yang gagal
        throw e; 
    }

    // 3. Jika berhasil, langsung lempar ke halaman utama (navbar)
    try {
        App.setRoot("navbar"); 
    } catch (IOException e) {
        e.printStackTrace();
        tampilkanPesan("Terjadi kesalahan saat memuat halaman aplikasi.");
    }

        } catch (SQLException e) {
            e.printStackTrace();
            // Menangkap jika username yang dimasukkan ternyata sudah ada di MySQL
            if (e.getMessage().contains("Duplicate entry")) {
                tampilkanPesan("Username '" + username + "' sudah terdaftar. Silakan cari username unik yang lain ya!");
            } else {
                tampilkanPesan("Gagal menyimpan data ke database: " + e.getMessage());
            }
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