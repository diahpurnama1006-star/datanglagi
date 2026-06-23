package model;

import java.time.LocalDate;

public class RiwayatHaid {
    private int idUser;
    private LocalDate tanggalMulai;
    private int durasiHaid;

    public RiwayatHaid(int idUser, LocalDate tanggalMulai, int durasiHaid) {
        this.idUser = idUser;
        this.tanggalMulai = tanggalMulai;
        this.durasiHaid = durasiHaid;
    }

    public int getIdUser() { return idUser; }
    public LocalDate getTanggalMulai() { return tanggalMulai; }
    public int getDurasiHaid() { return durasiHaid; }
}
