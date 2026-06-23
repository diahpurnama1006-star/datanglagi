package org.datanglagi;

public class UserSession {
    private static int idUser;
    private static String username;

    // Fungsi untuk menyimpan sesi setelah berhasil Login/Signup
    public static void setSession(int id, String name) {
        idUser = id;
        username = name;
    }

    public static int getIdUser() {
        return idUser;
    }

    public static String getUsername() {
        return username;
    }

    // Fungsi jika user menekan tombol Logout
    public static void clearSession() {
        idUser = 0;
        username = null;
    }
}