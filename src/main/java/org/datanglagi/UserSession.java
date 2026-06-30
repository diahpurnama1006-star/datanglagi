package org.datanglagi;

public class UserSession {
    private static UserSession instance;
    private String username, email;
    private int durasiHaid, panjangSiklus;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void startSession(String u, String e, int d, int p) {
        this.username = u; 
        this.email = e; 
        this.durasiHaid = d; 
        this.panjangSiklus = p;
    }

    // METHOD UNTUK LOGOUT / MENGHAPUS SESI
    public void clearSession() {
        this.username = null;
        this.email = null;
        this.durasiHaid = 0;
        this.panjangSiklus = 0;
        // instance = null; // Opsional: jika ingin me-reset instance sepenuhnya
    }

    // Getter dan Setter
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    
    public int getDurasiHaid() { return durasiHaid; }
    public void setDurasiHaid(int durasiHaid) { this.durasiHaid = durasiHaid; }

    public int getPanjangSiklus() { return panjangSiklus; }
    public void setPanjangSiklus(int panjangSiklus) { this.panjangSiklus = panjangSiklus; }
}