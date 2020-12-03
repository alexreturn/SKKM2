package com.stikom.skkm_mahasiswa.BEM.adapter;

public class adapterControlUser {

        private String nim;
        private String nama;
        private String jurusan;
        private String level;

    public adapterControlUser(String nim, String nama, String jurusan, String level) {
            this.nim = nim;
            this.nama = nama;
            this.jurusan = jurusan;
            this.level = level;
        }
        public String getnim() {
            return this.nim;
        }
        public String getnama() {
            return nama;
        }
        String getjurusan() {
            return jurusan;
        }
        String getlevel() {
            return level;
        }



}
