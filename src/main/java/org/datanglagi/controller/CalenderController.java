package org.datanglagi.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import org.datanglagi.DatabaseHalper;
import org.datanglagi.UserSession;

public class CalenderController {
    @FXML
    private ToggleButton btnmulai;

    @FXML
    private ToggleButton btnakhir;

    @FXML
    private GridPane calendarGrid;
    @FXML
    private Label monthLabel;

    private YearMonth currentMonth = YearMonth.now();

    // Data Siklus User yang diambil dari database
    private LocalDate hphtUser = null;
    private int panjangSiklusUser = 28;
    private int durasiHaidUser = 7;
    private LocalDate tempMulai = null;
    private LocalDate tempAkhir = null;

    @FXML
    public void initialize() {
        ambilDataSiklusDatabase();
        generateCalendar(currentMonth);
    }

    // Ambil data siklus haid terakhir dari database berdasarkan username aktif
    private void ambilDataSiklusDatabase() {
        String usernameAktif = UserSession.getInstance().getUsername();
        durasiHaidUser = UserSession.getInstance().getDurasiHaid();

        String query = "SELECT tanggal_mulai, panjang_siklus FROM siklus_haid WHERE username = ? ORDER BY id_siklus DESC LIMIT 1";

        try (Connection conn = DatabaseHalper.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, usernameAktif);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    if (rs.getDate("tanggal_mulai") != null) {
                        hphtUser = rs.getDate("tanggal_mulai").toLocalDate();
                    }
                    int siklusDb = rs.getInt("panjang_siklus");
                    if (siklusDb > 0)
                        panjangSiklusUser = siklusDb;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void prevMonth() {
        currentMonth = currentMonth.minusMonths(1);
        generateCalendar(currentMonth);
    }

    @FXML
    private void nextMonth() {
        currentMonth = currentMonth.plusMonths(1);
        generateCalendar(currentMonth);
    }

    private void generateCalendar(YearMonth yearMonth) {
        calendarGrid.getChildren().clear();

        monthLabel.setText(
                yearMonth.getMonth().getDisplayName(TextStyle.FULL, new Locale("id", "ID"))
                        + " " + yearMonth.getYear());

        LocalDate firstDay = yearMonth.atDay(1);
        int firstColumn = firstDay.getDayOfWeek().getValue() - 1;
        int daysInMonth = yearMonth.lengthOfMonth();
        int day = 1;

        LocalDate today = LocalDate.now();

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {

                if (row == 0 && col < firstColumn) {
                    continue;
                }

                if (day > daysInMonth) {
                    return;
                }

                // Buat tanggal objek penuh untuk hari yang sedang diproses loop
                LocalDate tanggalKalender = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), day);

                Button btnHari = new Button(String.valueOf(day));
                btnHari.setPrefSize(42, 42);

                // Tentukan Fase dan Warnanya
                String faseHariIni = hitungFaseSiklus(tanggalKalender);
                terapkanGayaWarna(btnHari, faseHariIni, tanggalKalender.equals(today));

                // Aksi saat tanggal kalender diklik oleh user
    btnHari.setOnAction(e -> {
    // CEK: Apakah user sedang dalam mode pilih tanggal?
    if (btnmulai.isSelected()) {
        tempMulai = tanggalKalender;
        tampilkanPesan("Berhasil", "Tanggal Mulai diset ke: " + tempMulai);
        btnmulai.setSelected(false);
    } else if (btnakhir.isSelected()) {
        tempAkhir = tanggalKalender;
        tampilkanPesan("Berhasil", "Tanggal Akhir diset ke: " + tempAkhir);
        btnakhir.setSelected(false);
        
        // Simpan jika kedua tanggal sudah dipilih
        if (tempMulai != null && tempAkhir != null) {
            simpanSiklusHaid(tempMulai, tempAkhir);
            tempMulai = null; 
            tempAkhir = null;
        }
    } else {
        // JIKA TIDAK SEDANG MEMILIH (Mode Normal), baru munculkan info fase
        tampilkanPesan("Informasi Siklus Tanggal " + tanggalKalender,
                       "Hari ini kamu diperkirakan berada dalam:\n★ " + faseHariIni + " ★");
    }
});

                calendarGrid.add(btnHari, col, row);
                day++;
            }
        }
    }

    // Fungsi Utama Penghitung Fase Siklus Wanita
    private String hitungFaseSiklus(LocalDate tanggal) {
        if (hphtUser == null) {
            return "Fase Normal (Belum ada data HPHT)";
        }

        // Hitung selisih hari dari HPHT terakhir ke tanggal kalender yang dituju
        long selisihHari = ChronoUnit.DAYS.between(hphtUser, tanggal);

        // Menormalkan selisih hari jika tanggal kalender berada di siklus-siklus bulan
        // berikutnya
        if (selisihHari >= 0) {
            selisihHari = selisihHari % panjangSiklusUser;
        } else {
            // Mengatasi perhitungan untuk bulan mundur sebelum HPHT
            selisihHari = panjangSiklusUser + (selisihHari % panjangSiklusUser);
            if (selisihHari == panjangSiklusUser)
                selisihHari = 0;
        }

        // Posisi Hari keberapa dalam siklus (dimulai dari Hari ke-1)
        long hariSiklus = selisihHari + 1;

        // 1. Logika Fase Menstruasi
        if (hariSiklus >= 1 && hariSiklus <= durasiHaidUser) {
            return "Fase Menstruasi";
        }

        // 2. Logika Fase Ovulasi / Masa Subur (Pertengahan Siklus, Rentang 5 hari)
        int hariOvulasiHPL = panjangSiklusUser - 14;
        if (hariSiklus >= (hariOvulasiHPL - 2) && hariSiklus <= (hariOvulasiHPL + 2)) {
            return "Fase Ovulasi (Masa Subur)";
        }

        // 3. Logika Fase Folikular (Di antara menstruasi dan ovulasi)
        if (hariSiklus > durasiHaidUser && hariSiklus < (hariOvulasiHPL - 2)) {
            return "Fase Folikular";
        }

        // 4. Sisanya adalah Fase Luteal (Setelah ovulasi menjelang haid berikutnya)
        return "Fase Luteal";
    }

    private void terapkanGayaWarna(Button btn, String fase, boolean apakahHariIni) {
        String styleBase = "-fx-background-radius: 12; -fx-border-radius: 12; -fx-font-size: 13;";

        if (apakahHariIni) {
            btn.setStyle(styleBase
                    + "-fx-background-color: #E2D3D5; -fx-text-fill: #6E1418; -fx-font-weight: bold; -fx-border-color: #6E1418; -fx-border-width: 2;");
            return;
        }

        switch (fase) {
            case "Fase Menstruasi":
                btn.setStyle(styleBase + "-fx-background-color: #6E1418; -fx-text-fill: white;");
                break;

            case "Fase Ovulasi (Masa Subur)":
                btn.setStyle(styleBase
                        + "-fx-background-color: #FFF3C4; -fx-text-fill: #A07200; -fx-border-color: #FFE082;");
                break;

            default:
                btn.setStyle(
                        styleBase + "-fx-background-color: white; -fx-text-fill: black; -fx-border-color: #E0E0E0;");
                break;
        }
    }
    
    private void simpanSiklusHaid(LocalDate mulai, LocalDate akhir) {
    String username = UserSession.getInstance().getUsername();
    // Hitung panjang siklus sederhana (selisih hari)
    long durasi = ChronoUnit.DAYS.between(mulai, akhir) + 1; 
    long panjangSiklus = 28; // Default, bisa disesuaikan nanti

    String sql = "INSERT INTO siklus_haid (username, tanggal_mulai, tanggal_akhir, panjang_siklus) VALUES (?, ?, ?, ?)";

    try (Connection conn = DatabaseHalper.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, username);
        stmt.setDate(2, java.sql.Date.valueOf(mulai));
        stmt.setDate(3, java.sql.Date.valueOf(akhir));
        stmt.setLong(4, panjangSiklus);
        stmt.executeUpdate();
        
        // Refresh data setelah simpan
        ambilDataSiklusDatabase();
        generateCalendar(currentMonth);
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
@FXML
private void handleToggleMulai() {
    if (btnmulai.isSelected()) {
        btnakhir.setSelected(false); // Pastikan tombol akhir mati saat mulai aktif
        tampilkanPesan("Mode Seleksi", "Silakan klik tanggal untuk TANGGAL MULAI");
    }
}
@FXML
private void handleToggleAkhir() {
    if (btnakhir.isSelected()) {
        btnmulai.setSelected(false); // Pastikan tombol mulai mati saat akhir aktif
        tampilkanPesan("Mode Seleksi", "Silakan klik tanggal untuk TANGGAL AKHIR");
    }
}

    private void tampilkanPesan(String judul, String isi) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Insight Siklus Mandiri");
        a.setHeaderText(judul);
        a.setContentText(isi);
        a.showAndWait();
    }
}