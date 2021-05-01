package com.stikom.skkm_mahasiswa.Config;

public class Config {
    public static final String URL = "http://192.168.0.104/";
    public static final String URL_FOTO = URL+"ApiSKKM2/";
    public static final String LOGIN_URL = URL+"ApiSKKM2/login.php";
    public static final String GET_KEGIATAN = URL+"ApiSKKM2/getKegiatan.php";
    public static final String GET_KEDUDUKAN = URL+"ApiSKKM2/getKedudukan.php?idJenis="; //idJenis=1&idSubJenis=1
    public static final String GET_BOBOT = URL+"ApiSKKM2/getBobot.php?idJenis="; //?idJenis=1&idSubJenis=1&idKedudukan=1&idTingkat=1
    public static final String SKKM_URL = URL+"ApiSKKM2/getSKKM.php?nim=";
    public static final String USER_URL = URL+"ApiSKKM2/getUser.php?nim=";
    public static final String SIMPANSKKM = URL+"ApiSKKM2/simpanSKKM.php";
    public static final String getJenisKegiatan = URL+"ApiSKKM2/getJenisKegiatan.php";
    public static final String getJenisSubKegiatan = URL+"ApiSKKM2/getJenisSubKegiatan.php?idJenis=";
    public static final String getTingkat = URL+"ApiSKKM2/getTingkat.php";
    public static final String EVENT_URL = URL+"ApiSKKM2/getEvent.php";

    ///pkm//
    public static final String GET_PKM = URL+"ApiSKKM2/getPKM.php?nim=";
    public static final String simpanPKMproposal = URL+"ApiSKKM2/simpanPKMproposal.php";
    public static final String upLoadServerUri = URL+"ApiSKKM2/UploadToServer.php";


    //////

    public static final String SEMINAR_URL = URL+"ApiSKKM2/getSeminar.php";
    public static final String getAllUser = URL+"ApiSKKM2/getAllUser.php";
    public static final String getSKKMbem = URL+"ApiSKKM2/getSKKMbem.php?nim=";
    //////////////////////////////USER/////////////////////////////////////////
    public static final String KEY_USER_nim= "nim";
    public static final String KEY_USER_nama= "nama";
    public static final String KEY_USER_alamat= "alamat";
    public static final String KEY_USER_jurusan= "jurusan";
    public static final String KEY_USER_level= "level";

    public static final String TAG_USER_nim= "nim";
    public static final String TAG_USER_nama= "nama";
    public static final String TAG_USER_alamat= "alamat";
    public static final String TAG_USER_jurusan= "jurusan";
    public static final String TAG_USER_level= "level";
    //////////////////////////////Login/////////////////////////////////////////

    public static final String KEY_EMP_nim= "nim";
    public static final String KEY_EMP_nama_user= "nama_user";
    public static final String KEY_EMP_telp_user= "telp_user";
    public static final String KEY_EMP_password= "password";
    public static final String KEY_EMP_status= "status";

    //------------------------------KEGIATAN-----------------------------------------



    public static final String KEY_EMP_KEGIATAN_id ="id";
    public static final String KEY_EMP_KEGIATAN_jenisUser = "jenisUser";
    public static final String KEY_EMP_KEGIATAN_idUser = "idUser";
    public static final String KEY_EMP_KEGIATAN_idJenis = "idJenis";
    public static final String KEY_EMP_KEGIATAN_idSubJenis = "idSubJenis";
    public static final String KEY_EMP_KEGIATAN_idTingkat = "idTingkat";
    public static final String KEY_EMP_KEGIATAN_namaKegiatan = "namaKegiatan";
    public static final String KEY_EMP_KEGIATAN_tanggalMulai = "tanggalMulai";
    public static final String KEY_EMP_KEGIATAN_tanggalSelesai = "tanggalSelesai";
    public static final String KEY_EMP_KEGIATAN_tanggalPembuatan ="tanggalPembuatan";
    public static final String KEY_EMP_KEGIATAN_keterangan ="keterangan";
    public static final String KEY_EMP_KEGIATAN_namaJenis ="namaJenis";
    public static final String KEY_EMP_KEGIATAN_namaSubJenis ="namaSubJenis";
    public static final String KEY_EMP_KEGIATAN_namaTingkat ="namaTingkat";
    public static final String KEY_EMP_KEGIATAN_custom ="custom";



    public static final String TAG_EMP_KEGIATAN_id ="id";
    public static final String TAG_EMP_KEGIATAN_jenisUser = "jenisUser";
    public static final String TAG_EMP_KEGIATAN_idUser = "idUser";
    public static final String TAG_EMP_KEGIATAN_idJenis = "idJenis";
    public static final String TAG_EMP_KEGIATAN_idSubJenis = "idSubJenis";
    public static final String TAG_EMP_KEGIATAN_idTingkat = "idTingkat";
    public static final String TAG_EMP_KEGIATAN_namaKegiatan = "namaKegiatan";
    public static final String TAG_EMP_KEGIATAN_tanggalMulai = "tanggalMulai";
    public static final String TAG_EMP_KEGIATAN_tanggalSelesai = "tanggalSelesai";
    public static final String TAG_EMP_KEGIATAN_tanggalPembuatan ="tanggalPembuatan";
    public static final String TAG_EMP_KEGIATAN_keterangan ="keterangan";
    public static final String TAG_EMP_KEGIATAN_namaJenis ="namaJenis";
    public static final String TAG_EMP_KEGIATAN_namaSubJenis ="namaSubJenis";
    public static final String TAG_EMP_KEGIATAN_namaTingkat ="namaTingkat";
    public static final String TAG_EMP_KEGIATAN_custom ="custom";
    //-------------------------------------------------------------------------------

    public static final String KEY_TAG_namaKegiatan ="namaKegiatan";
    public static final String KEY_TAG_namaTingkat ="namaTingkat";
    public static final String KEY_TAG_namaKedudukan ="namaKedudukan";
    public static final String KEY_TAG_poin ="poin";

    //-------------------------------------------------------------------------------
    public static final String KEY_EVENT_ID= "id";
    public static final String KEY_EVENT_JUDUL= "judul";
    public static final String KEY_EVENT_EVENT= "event";
    public static final String KEY_EVENT_EVENTDATE= "eventdate";
    public static final String KEY_EVENT_TANGGAL= "tanggal";
    public static final String TAG_EVENT_LEVEL= "level";

    public static final String TAG_EVENT_ID= "id";
    public static final String TAG_EVENT_JUDUL= "judul";
    public static final String TAG_EVENT_EVENT= "event";
    public static final String TAG_EVENT_EVENTDATE= "eventdate";
    public static final String TAG_EVENT_TANGGAL= "tanggal";
    //-------------------------------------------------------------------------------
    public static final String KEY_SEMINAR_ID= "id";
    public static final String KEY_SEMINAR_JUDUL= "judul";
    public static final String KEY_SEMINAR_DESKRIPSI= "deskripsi";
    public static final String KEY_SEMINAR_PEOPLE= "people";
    public static final String KEY_SEMINAR_IMAGE= "image";
    public static final String KEY_SEMINAR_STARTDATE= "startDate";
    public static final String KEY_SEMINAR_ENDDATE= "endDate";
    public static final String KEY_SEMINAR_TANGGAL= "tanggal";

    public static final String TAG_SEMINAR_ID= "id";
    public static final String TAG_SEMINAR_JUDUL= "judul";
    public static final String TAG_SEMINAR_DESKRIPSI= "deskripsi";
    public static final String TAG_SEMINAR_PEOPLE= "people";
    public static final String TAG_SEMINAR_IMAGE= "image";
    public static final String TAG_SEMINAR_STARTDATE= "startDate";
    public static final String TAG_SEMINAR_ENDDATE= "endDate";
    public static final String TAG_SEMINAR_TANGGAL= "tanggal";
    //-------------------------------------------------------------------------------

    public static final String KEY_SKKM_id= "id";
    public static final String KEY_SKKM_nim_user= "nim_user";
    public static final String KEY_SKKM_judul= "judul";
    public static final String KEY_SKKM_deskripsi= "deskripsi";
    public static final String KEY_SKKM_kategori= "kategori";
    public static final String KEY_SKKM_partisipasi= "partisipasi";
    public static final String KEY_SKKM_tingkat= "tingkat";
    public static final String KEY_SKKM_image= "image";
    public static final String KEY_SKKM_poin= "poin";
    public static final String KEY_SKKM_seminarDate= "seminarDate";
    public static final String KEY_SKKM_tanggal= "tanggal";
    public static final String KEY_SKKM_status= "status";

    public static final String TAG_SKKM_id= "id";
    public static final String TAG_SKKM_nim_user= "nim_user";
    public static final String TAG_SKKM_judul= "judul";
    public static final String TAG_SKKM_deskripsi= "deskripsi";
    public static final String TAG_SKKM_kategori= "kategori";
    public static final String TAG_SKKM_image= "image";
    public static final String TAG_SKKM_poin= "poin";
    public static final String TAG_SKKM_seminarDate= "seminarDate";
    public static final String TAG_SKKM_tanggal= "tanggal";
    public static final String TAG_SKKM_status= "status";
    //-------------------------------------------------------------------------------

    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_JSON_ARRAY2="result2";
    public static final String LOGIN_SUCCESS = "success";
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
    public static final String SHARED_PREF_NAME = "myloginapp";
    public static final String NIM_DETAIL_SHARED_PREF = "NIMDETAIL";
    public static final String NAMA_DETAIL_SHARED_PREF = "NAMA";
    public static final String NIM_SHARED_PREF = "NIM";
    public static final String password_SHARED_PREF = "password";
    public static final String nama_SHARED_PREF = "nama";
    public static final String alamat_SHARED_PREF = "alamat";
    public static final String jurusan_SHARED_PREF = "jurusan";
    public static final String prodi_SHARED_PREF = "prodi";
    public static final String angkatan_SHARED_PREF = "angkatan";
    public static final String semester_SHARED_PREF = "semester";
    public static final String level_SHARED_PREF = "level";
    public static final String foto_SHARED_PREF = "foto";
    public static final String panjangdata_SHARED_PREF = "panjangdata]";

}
