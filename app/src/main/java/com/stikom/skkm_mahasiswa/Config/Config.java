package com.stikom.skkm_mahasiswa.Config;

public class Config {
    public static final String URL = "http://192.168.18.31/";
    public static final String LOGIN_URL = URL+"ApiSKKM/login.php";
    public static final String USER_URL = URL+"ApiSKKM/getUser.php?nim=";
    public static final String EVENT_URL = URL+"ApiSKKM/getEvent.php";
    public static final String SEMINAR_URL = URL+"ApiSKKM/getSeminar.php";

    //////////////////////////////Login/////////////////////////////////////////

    public static final String KEY_EMP_nim= "nim";
    public static final String KEY_EMP_nama_user= "nama_user";
    public static final String KEY_EMP_telp_user= "telp_user";
    public static final String KEY_EMP_password= "password";
    public static final String KEY_EMP_status= "status";

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
