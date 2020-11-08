package com.stikom.skkm_mahasiswa.Config;

public class Config {
    public static final String URL = "http://192.168.18.31/";
    public static final String LOGIN_URL = URL+"ApiSKKM/login.php";
    public static final String USER_URL = URL+"ApiSKKM/getUser.php?nim=";
    public static final String EVENT_URL = URL+"ApiSKKM/getEvent.php";
    public static final String SEMINAR_URL = URL+"ApiSKKM/getSeminar.php";
    public static final String SKKM_URL = URL+"ApiSKKM/getSKKM.php?nim=";

    //////////////////////////////Login/////////////////////////////////////////

    public static final String KEY_EMP_nim= "nim";
    public static final String KEY_EMP_nama_user= "nama_user";
    public static final String KEY_EMP_telp_user= "telp_user";
    public static final String KEY_EMP_password= "password";
    public static final String KEY_EMP_status= "status";

    //-------------------------------------------------------------------------------
    public static final String KEY_EVENT_ID= "id";
    public static final String KEY_EVENT_JUDUL= "judul";
    public static final String KEY_EVENT_EVENT= "event";
    public static final String KEY_EVENT_EVENTDATE= "eventdate";
    public static final String KEY_EVENT_TANGGAL= "tanggal";

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
    public static final String KEY_SEMINAR_SEMINARDATE= "seminarDate";
    public static final String KEY_SEMINAR_TANGGAL= "tanggal";

    public static final String TAG_SEMINAR_ID= "id";
    public static final String TAG_SEMINAR_JUDUL= "judul";
    public static final String TAG_SEMINAR_DESKRIPSI= "deskripsi";
    public static final String TAG_SEMINAR_PEOPLE= "people";
    public static final String TAG_SEMINAR_IMAGE= "image";
    public static final String TAG_SEMINAR_SEMINARDATE= "seminarDate";
    public static final String TAG_SEMINAR_TANGGAL= "tanggal";
    //-------------------------------------------------------------------------------

    public static final String KEY_SKKM_id= "id";
    public static final String KEY_SKKM_nim_user= "nim_user";
    public static final String KEY_SKKM_judul= "judul";
    public static final String KEY_SKKM_deskripsi= "deskripsi";
    public static final String KEY_SKKM_kategori= "kategori";
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
    public static final String LOGIN_SUCCESS = "success";
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
    public static final String SHARED_PREF_NAME = "myloginapp";
    public static final String NIM_SHARED_PREF = "NIM";
    public static final String password_SHARED_PREF = "password";
    public static final String nama_SHARED_PREF = "nama";
    public static final String alamat_SHARED_PREF = "alamat";
    public static final String jurusan_SHARED_PREF = "jurusan";

}
