package org.datanglagi.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;

public class InputharianController {

    // Menyambungkan komponen FXML dengan variabel Java
    @FXML
    private ToggleButton toggleKb;

    @FXML
    public void initialize() {
        // Kondisi awal di-set OFF saat halaman dibuka
        toggleKb.setSelected(false);
    }

    @FXML
    private void handleToggleKb() {
        if (toggleKb.isSelected()) {
            // Ketika tombol dalam posisi AKTIF (ON)
            toggleKb.setText("ON");
            // Ubah warnanya menjadi merah marun khas DatangLagi saat menyala
            toggleKb.setStyle("-fx-background-radius: 20; -fx-background-color: #9B1C1C; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
            System.out.println("Status KB: Diminum");
        } else {
            // Ketika tombol dimatikan kembali (OFF)
            toggleKb.setText("OFF");
            // Kembalikan warnanya menjadi abu-abu kusam
            toggleKb.setStyle("-fx-background-radius: 20; -fx-background-color: #D3D3D3; -fx-text-fill: #555; -fx-font-weight: bold; -fx-cursor: hand;");
            System.out.println("Status KB: Tidak Diminum");
        }
    }
}