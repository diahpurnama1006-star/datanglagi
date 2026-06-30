package org.datanglagi.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.datanglagi.App;
import java.io.IOException;

public class InputharianController {

    @FXML private Label lblHari;
    @FXML private Button btnTidakAda, btnRingan, btnSedang, btnBerat;
    @FXML private CheckBox cbNyeriPerut, cbKembung, cbPayudaraNyeri, cbSakitKepala, cbKram, cbMual, cbLemas;
    @FXML private TextArea txtCatatan;
    @FXML private ToggleButton btnStatusKB; 

    private String aliranTerpilih = "Tidak Ada";

    @FXML
    public void initialize() {
        LocalDate hariIni = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", new Locale("id", "ID"));
        if (lblHari != null) lblHari.setText(hariIni.format(formatter));
        aturWarnaButtonAliran(btnTidakAda);
    }

    // Fungsi klik button (Pastikan di Scene Builder sudah terhubung ke fungsi ini)
    @FXML private void klikTidakAda(MouseEvent event) { aliranTerpilih = "Tidak Ada"; aturWarnaButtonAliran(btnTidakAda); }
    @FXML private void klikRingan(MouseEvent event) { aliranTerpilih = "Ringan"; aturWarnaButtonAliran(btnRingan); }
    @FXML private void klikSedang(MouseEvent event) { aliranTerpilih = "Sedang"; aturWarnaButtonAliran(btnSedang); }
    @FXML private void klikBerat(MouseEvent event) { aliranTerpilih = "Berat"; aturWarnaButtonAliran(btnBerat); }

    private void aturWarnaButtonAliran(Button btnAktif) {
        Button[] semuaButton = {btnTidakAda, btnRingan, btnSedang, btnBerat};
        for (Button btn : semuaButton) {
            if (btn != null) {
                btn.setStyle(btn == btnAktif ? 
                    "-fx-background-color: #6E1418; -fx-text-fill: #FFFFFF; -fx-background-radius: 15;" : 
                    "-fx-background-color: #F1EFEF; -fx-text-fill: #000000; -fx-background-radius: 15;");
            }
        }
    }

    @FXML
    private void handleSimpan() {
        tampilkanPesan("Catatanmu sudah dicatat, semoga lekas membaik!");
        
        try {
            App.setRoot("navbar"); 
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