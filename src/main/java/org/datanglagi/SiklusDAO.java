package org.datanglagi;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class SiklusDAO {

    public Map<String, Object> hitungLogikaSiklus(String username) {
        Map<String, Object> hasilKalkulasi = new HashMap<>();

        // Default values agar tidak null di UI
        hasilKalkulasi.put("panjangSiklus", 28);
        hasilKalkulasi.put("durasiHaidNyata", 0);
        hasilKalkulasi.put("prediksiBerikutnya", null);
        hasilKalkulasi.put("hariMenujuHaid", 0);
        hasilKalkulasi.put("tglOvulasi", null);
        hasilKalkulasi.put("faseHariIni", "Data belum cukup");

        String query = "SELECT tanggal_mulai, tanggal_akhir FROM siklus_haid WHERE username = ? ORDER BY tanggal_mulai DESC LIMIT 2";

        LocalDate hphTerakhir = null;
        LocalDate hphBulanLalu = null;
        LocalDate tglAkhirTerakhir = null;

        try (Connection conn = DatabaseHalper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    hphTerakhir = rs.getDate("tanggal_mulai") != null ? rs.getDate("tanggal_mulai").toLocalDate() : null;
                    tglAkhirTerakhir = rs.getDate("tanggal_akhir") != null ? rs.getDate("tanggal_akhir").toLocalDate() : null;
                }
                if (rs.next()) {
                    hphBulanLalu = rs.getDate("tanggal_mulai") != null ? rs.getDate("tanggal_mulai").toLocalDate() : null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // FIX: ambil panjang siklus default dari session, dengan fallback 28 jika session belum pernah di-set (0)
        long panjangSiklusDefault = UserSession.getInstance().getPanjangSiklus();
        if (panjangSiklusDefault <= 0) {
            panjangSiklusDefault = 28;
        }

        if (hphTerakhir == null) {
            // FIX: Belum ada data sama sekali -> tetap kasih estimasi kasar, bukan dibiarkan null
            LocalDate prediksiKasar = SiklusKalkulator.hitungPrediksiHaidBerikutnya(null, panjangSiklusDefault);
            long hariMenujuHaid = SiklusKalkulator.hitungHariMenujuHaid(LocalDate.now(), prediksiKasar);
            LocalDate tglOvulasi = SiklusKalkulator.hitungHariOvulasi(prediksiKasar);

            hasilKalkulasi.put("panjangSiklus", panjangSiklusDefault);
            hasilKalkulasi.put("prediksiBerikutnya", prediksiKasar);
            hasilKalkulasi.put("hariMenujuHaid", hariMenujuHaid);
            hasilKalkulasi.put("tglOvulasi", tglOvulasi);
            hasilKalkulasi.put("faseHariIni", "Estimasi awal (belum ada riwayat siklus)");
            return hasilKalkulasi;
        }

        // Ada setidaknya 1 data HPH di database
        long panjangSiklus = panjangSiklusDefault;
        if (hphBulanLalu != null) {
            long durasiDiukur = SiklusKalkulator.hitungPanjangSiklus(hphBulanLalu, hphTerakhir);
            // Hanya gunakan durasi dari DB jika angka masuk akal (misal 20-45 hari)
            if (durasiDiukur >= 20 && durasiDiukur <= 45) {
                panjangSiklus = durasiDiukur;
            }
        }

        long durasiHaidNyata = (tglAkhirTerakhir != null)
                ? SiklusKalkulator.hitungDurasiHaidNyata(hphTerakhir, tglAkhirTerakhir)
                : UserSession.getInstance().getDurasiHaid();

        LocalDate prediksiBerikutnya = SiklusKalkulator.hitungPrediksiHaidBerikutnya(hphTerakhir, panjangSiklus);
        long hariMenujuHaid = SiklusKalkulator.hitungHariMenujuHaid(LocalDate.now(), prediksiBerikutnya);
        LocalDate tglOvulasi = SiklusKalkulator.hitungHariOvulasi(prediksiBerikutnya);

        String faseHariIni = SiklusKalkulator.cekFaseKalender(LocalDate.now(), hphTerakhir, tglOvulasi, prediksiBerikutnya);

        hasilKalkulasi.put("panjangSiklus", panjangSiklus);
        hasilKalkulasi.put("durasiHaidNyata", durasiHaidNyata);
        hasilKalkulasi.put("prediksiBerikutnya", prediksiBerikutnya);
        hasilKalkulasi.put("hariMenujuHaid", hariMenujuHaid);
        hasilKalkulasi.put("tglOvulasi", tglOvulasi);
        hasilKalkulasi.put("faseHariIni", faseHariIni);

        return hasilKalkulasi;
    }
}