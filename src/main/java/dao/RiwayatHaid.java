package dao;

import java.time.LocalDate;

public class RiwayatHaid {
    private int idUser;
    private LocalDate tanggalMulai;
    private int durasiHaid;

    // Constructor utama
    public RiwayatHaid(int idUser, LocalDate tanggalMulai, int durasiHaid) {
        this.idUser = idUser;
        this.tanggalMulai = tanggalMulai;
        this.durasiHaid = durasiHaid;
    }

    // Getter dan Setter standar
    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }

    public LocalDate getTanggalMulai() { return tanggalMulai; }
    public void setTanggalMulai(LocalDate tanggalMulai) { this.tanggalMulai = tanggalMulai; }

    public int getDurasiHaid() { return durasiHaid; }
    public void setDurasiHaid(int durasiHaid) { this.durasiHaid = durasiHaid; }
}