package org.datanglagi.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.datanglagi.App;
import org.datanglagi.UserSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

public class HomepageController {
    
    @FXML private Label labelHaloUser;       
    @FXML private Label labelTanggalHariIni;  
    @FXML private Label labelHariLagi;        
    @FXML private Label labelTanggalPrediksi; 
    @FXML private Label labelPanjangSiklus;   
    @FXML private Label labelDurasiHaid;      
    @FXML private Label labelHariOvulasi;     
    @FXML private Label labelSiklusDicatat;   

    // Membuat objek mesin kalkulasi dari package DAO yang benar secara mutlak
    private final dao.SiklusDAO siklusDAO = new dao.SiklusDAO();

    @FXML
    public void initialize() {
        // 1. Set nama user aktif & tanggal hari ini
        String namaAktif = UserSession.getUsername();
        labelHaloUser.setText(namaAktif != null && !namaAktif.isEmpty() ? "Halo " + namaAktif : "Halo User");

        LocalDate hariIni = LocalDate.now();
        DateTimeFormatter formatHariIni = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", new Locale("id", "ID"));
        labelTanggalHariIni.setText(hariIni.format(formatHariIni));

        // 2. Kalkulasi Data Siklus menggunakan objek siklusDAO yang aman
        int idUserLogin = UserSession.getIdUser();
        Map<String, Object> dataSiklus = siklusDAO.hitungLogikaSiklus(idUserLogin);

        String fase = (String) dataSiklus.get("fase");
        LocalDate tglPrediksi = (LocalDate) dataSiklus.get("prediksiDatangLagi");

        // 3. Tampilkan Sisa Hari ke Card Utama
        DateTimeFormatter formatPrediksi = DateTimeFormatter.ofPattern("d MMMM yyyy", new Locale("id", "ID"));
        labelTanggalPrediksi.setText(tglPrediksi.format(formatPrediksi));

        if ("Menstruasi".equals(fase)) {
            labelHariLagi.setText("Sedang Haid");
        } else {
            long sisaHari = java.time.temporal.ChronoUnit.DAYS.between(hariIni, tglPrediksi);
            if (sisaHari < 0) {
                labelHariLagi.setText("Terlambat " + Math.abs(sisaHari) + " hari");
            } else if (sisaHari == 0) {
                labelHariLagi.setText("Hari ini!");
            } else {
                labelHariLagi.setText(sisaHari + " hari lagi");
            }
        }

        // 4. Ambil Durasi Haid untuk Ringkasan Siklus di Bawah
        int durasiHaidUser = 5; 
        try (Connection conn = config.DatabaseHalper.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT durasi_haid FROM riwayat_haid WHERE id_user = ? ORDER BY tanggal_mulai DESC LIMIT 1")) {
            stmt.setInt(1, idUserLogin);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    durasiHaidUser = rs.getInt("durasi_haid");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        labelPanjangSiklus.setText("28 Hari");
        labelDurasiHaid.setText(durasiHaidUser + " Hari");
        labelHariOvulasi.setText("14");
        labelSiklusDicatat.setText("1 Kali");
    }

    // ====================================================
    // FXML METHOD (LOGIKA NAVIGASI MENU KLIK)
    // ====================================================
    @FXML
    private void keBeranda(MouseEvent event) throws IOException {
        App.setRoot("homepage");
    }

    @FXML
    private void keKalender(MouseEvent event) throws IOException {
        App.setRoot("calender");
    }

    @FXML
    private void kePerawatan(MouseEvent event) throws IOException {
        App.setRoot("perawatan");
    }

    @FXML
    private void keAkun(MouseEvent event) throws IOException {
        App.setRoot("user");
    }

    @FXML
    public void handleTambahHubungan() {
        System.out.println("Tombol hubungan pasangan diklik!");
    }
}