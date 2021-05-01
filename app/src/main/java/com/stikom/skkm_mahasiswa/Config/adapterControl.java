package com.stikom.skkm_mahasiswa.Config;

public class adapterControl {

        private String id;
        private String judul;
        private String deskripsi;
        private String partisipasi;
        private String tingkat;
        private String image;
        private String seminardate;
        private String date;
        private String kategori;
        private String poin;
        private String status;

    public adapterControl(String id, String judul, String deskripsi, String partisipasi, String tingkat, String image, String seminardate, String date, String poin, String kategori, String status) {
            this.id = id;
            this.judul = judul;
            this.deskripsi = deskripsi;
            this.partisipasi = partisipasi;
            this.tingkat = tingkat;
            this.image = image;
            this.seminardate = seminardate;
            this.date = date;
            this.poin = poin;
            this.kategori = kategori;
            this.status = status;
        }
        String getid() {
            return this.id;
        }
    public  String getjudul() {
            return judul;
        }
        String getdeskripsi() {
            return deskripsi;
        }
    public    String getpartisipasi() {
            return partisipasi;
        }
    public  String gettingkat() {
            return tingkat;
        }
        String getimage() {
            return image;
        }
        String getseminardate() {
            return seminardate;
        }
        String getdate() {
            return date;
        }
        public String getpoin() {
            return poin;
        }
        String getkategori() {
            return kategori;
        }
        String getstatus() {
            return status;
        }



}
