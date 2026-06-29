package org.datanglagi.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import org.datanglagi.App;
import org.datanglagi.DatabaseHalper;
import org.datanglagi.UserSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PerawatanController {

    @FXML
    private BarChart<String, Number> chartInsight;

    @FXML
    public void initialize() {
        muatDataGrafik();
    }

    private void muatDataGrafik() {
        String usernameAktif = UserSession.getInstance().getUsername();

        String query = "SELECT bulan, panjang_siklus FROM siklus_haid WHERE username = ? ORDER BY id_siklus ASC LIMIT 3";

        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
        dataSeries.setName("Panjang Siklus (Hari)");

        try (Connection conn = DatabaseHalper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, usernameAktif);

            try (ResultSet rs = stmt.executeQuery()) {
                int jumlahData = 0;

                while (rs.next()) {
                    String bulan = rs.getString("bulan");
                    int panjangSiklus = rs.getInt("panjang_siklus");

                    dataSeries.getData().add(new XYChart.Data<>(bulan, panjangSiklus));
                    jumlahData++;
                }

                if (jumlahData == 0) {
                    int durasiStr = UserSession.getInstance().getDurasiStr();
                    dataSeries.getData().add(new XYChart.Data<>("Bulan Ini", durasiStr));
                }
            }

            // 1. Masukkan data ke dalam BarChart
            chartInsight.getData().clear();
            chartInsight.getData().add(dataSeries);

            // 2. LOGIKA BARU: Warnai semua batang grafik menjadi Merah Tua (#6E1418)
            for (XYChart.Data<String, Number> data : dataSeries.getData()) {
                if (data.getNode() != null) {
                    // Mengubah warna isi batang dan warna border-nya agar senada
                    data.getNode().setStyle("-fx-bar-fill: #6E1418; -fx-background-color: #6E1418;");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("[ERROR] Gagal memuat data insight dari database.");
        }
    }

    @FXML
    private void keTipsNyeri(MouseEvent event) {
        try {
            NavbarController.loadPage("perawatan");
        } catch (Exception e) {
            e.printStackTrace();
            tampilkanPesan("Gagal memuat halaman Tips Nyeri.");
        }
    }

    @FXML
    private void keTipsDiet(MouseEvent event) {
        try {
            NavbarController.loadPage("diet");
        } catch (Exception e) {
            e.printStackTrace();
            tampilkanPesan("Gagal memuat halaman Tips Diet.");
        }
    }

    @FXML
    private void keTipsOlahraga(MouseEvent event) {
        try {
            NavbarController.loadPage("olga");
        } catch (Exception e) {
            e.printStackTrace();
            tampilkanPesan("Gagal memuat halaman Tips Olahraga.");
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