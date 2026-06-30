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

    // Hanya lakukan kalkulasi jika setidaknya ada data HPHT (Haid Pertama Hari Terakhir)
    if (hphTerakhir != null) {
        // Gunakan panjang siklus dari Session jika data riwayat bulan lalu tidak ada
        long panjangSiklus = (hphBulanLalu != null) ? 
                             SiklusKalkulator.hitungPanjangSiklus(hphBulanLalu, hphTerakhir) : 
                             UserSession.getInstance().getPanjangSiklus(); // Ambil dari sesi (default)

        long durasiHaidNyata = (tglAkhirTerakhir != null) ? 
                               SiklusKalkulator.hitungDurasiHaidNyata(hphTerakhir, tglAkhirTerakhir) : 
                               UserSession.getInstance().getDurasiHaid();

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
    }

    return hasilKalkulasi;
}
}