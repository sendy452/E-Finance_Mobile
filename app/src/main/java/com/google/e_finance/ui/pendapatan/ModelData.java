package com.google.e_finance.ui.pendapatan;

public class ModelData {
    String id_pemasukan, tgl_pemasukan, jumlah, keterangan, id_user;

    public ModelData() {
    }

    /**
     * Dekalarasi variabel sesuai dengan kolom database
     */
    public ModelData(String id_pemasukan, String tgl_pemasukan, String jumlah, String keterangan, String id_user) {
        this.id_pemasukan = id_pemasukan;
        this.tgl_pemasukan = tgl_pemasukan;
        this.jumlah = jumlah;
        this.keterangan = keterangan;
        this.id_user = id_user;
    }

    /**
     * Untuk function get adalah mengambil data dari database
     * function set untuk mengembalikan nilai data ke database
     */
    public String getId() {

        return id_pemasukan;
    }

    public void setId(String id_pemasukan) {

        this.id_pemasukan = id_pemasukan;
    }

    public String getTgl() {
        return tgl_pemasukan;
    }

    public void setTgl(String tgl_pemasukan) {

        this.tgl_pemasukan = tgl_pemasukan;
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