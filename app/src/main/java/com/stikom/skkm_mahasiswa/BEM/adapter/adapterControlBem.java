package com.stikom.skkm_mahasiswa.BEM.adapter;

public class adapterControlBem {

        private String id;
        private String judul;
        private String deskripsi;
        private String image;
        private String seminardate;
        private String date;
        private String kategori;
        private String poin;
        private String status;

    public adapterControlBem(String id, String judul, String deskripsi, String image, String seminardate, String date, String poin, String kategori, String status) {
            this.id = id;
            this.judul = judul;
            this.deskripsi = deskripsi;
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
        String getjudul() {
            return judul;
        }
        String getdeskripsi() {
            return deskripsi;
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
        String getpoin() {
            return poin;
        }
        String getkategori() {
            return kategori;
        }
        String getstatus() {
            return status;
        }



}
