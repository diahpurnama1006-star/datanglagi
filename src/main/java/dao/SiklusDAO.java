
package dao;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import config.DatabaseHalper;

public class SiklusDAO {

    public Map<String, Object> hitungLogikaSiklus(int idUser) {
        Map<String, Object> hasilKalkulasi = new HashMap<>();
        String query = "SELECT tanggal_mulai, durasi_haid FROM riwayat_haid WHERE id_user = ? ORDER BY tanggal_mulai DESC LIMIT 1";
        
        LocalDate hpht = null;
        int durasiHaid = 7;

        try (Connection conn = DatabaseHalper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, idUser);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    hpht = rs.getDate("tanggal_mulai").toLocalDate();
                    durasiHaid = rs.getInt("durasi_haid");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (hpht == null) {
            hasilKalkulasi.put("fase", "Belum Ada Data");
            hasilKalkulasi.put("hariKe", 0);
            hasilKalkulasi.put("prediksiDatangLagi", LocalDate.now());
            return hasilKalkulasi;
        }

        long hariKe = ChronoUnit.DAYS.between(hpht, LocalDate.now()) + 1;
        int panjangSiklusDefault = 28; 

        String faseHariIni;
        if (hariKe >= 1 && hariKe <= durasiHaid) {
            faseHariIni = "Menstruasi";
        } else if (hariKe > durasiHaid && hariKe <= 11) {
            faseHariIni = "Folikular";
        } else if (hariKe >= 12 && hariKe <= 16) {
            faseHariIni = "Ovulasi";
        } else if (hariKe >= 17 && hariKe <= panjangSiklusDefault) {
            faseHariIni = "Luteal";
        } else {
            faseHariIni = "Menstruasi (Siklus Baru/Terlambat)";
        }

        LocalDate prediksiDatangLagi = hpht.plusDays(panjangSiklusDefault);

        hasilKalkulasi.put("fase", faseHariIni);
        hasilKalkulasi.put("hariKe", (int) hariKe);
        hasilKalkulasi.put("prediksiDatangLagi", prediksiDatangLagi);

        return hasilKalkulasi;
    }
}