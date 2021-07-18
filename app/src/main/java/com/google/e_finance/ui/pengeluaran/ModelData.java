package com.google.e_finance.ui.pengeluaran;

public class ModelData {
    String id_pengeluaran, tgl_pengeluaran, jumlah, keterangan, id_user;

    public ModelData() {
    }

    /**
     * Dekalarasi variabel sesuai dengan kolom database
     */
    public ModelData(String id_pengeluaran, String tgl_pengeluaran, String jumlah, String keterangan, String id_user) {
        this.id_pengeluaran = id_pengeluaran;
        this.tgl_pengeluaran = tgl_pengeluaran;
        this.jumlah = jumlah;
        this.keterangan = keterangan;
        this.id_user = id_user;
    }

    /**
     * Untuk function get adalah mengambil data dari database
     * function set untuk mengembalikan nilai data ke database
     */
    public String getId() {

        return id_pengeluaran;
    }

    public void setId(String id_pengeluaran) {

        this.id_pengeluaran = id_pengeluaran;
    }

    public String getTgl() {
        return tgl_pengeluaran;
    }

    public void setTgl(String tgl_pengeluaran) {

        this.tgl_pengeluaran = tgl_pengeluaran;
    }

    public String getJumlah() {

        return jumlah;
    }

    public void setJumlah(String jumlah) {

        this.jumlah = jumlah;
    }

    public String getKeterangan() {

        return keterangan;
    }

    public void setKeterangan(String keterangan) {

        this.keterangan = keterangan;
    }
    public String getIduser() {

        return id_user;
    }

    public void setIduser(String id_user) {

        this.id_user = id_user;
    }
}