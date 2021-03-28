package com.stikom.skkm_mahasiswa;

import android.app.Activity;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stikom.skkm_mahasiswa.Config.Config;
import com.stikom.skkm_mahasiswa.Config.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity  implements View.OnClickListener  {

    private Dialog customDialog;
    Boolean loggedIn= false;
    Button btnlogin;
    EditText txtNim,txtPassword;
    private ProgressDialog loading;
    int success;
    private String JSON_STRING;
    String nama_user,alamat,jurusan,foto,prodi,angkatan,semester,level;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        txtNim = (EditText) findViewById(R.id.nim);
        txtPassword = (EditText) findViewById(R.id.password);

        btnlogin=(Button)findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(this);

        initCustomDialog();

    };
    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences sharedPreferences=getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        loggedIn=sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);


        if (loggedIn==true){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }


    }
    @Override
    public void onClick(View v) {
//        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//        startActivity(intent);
        login();
    }

    private void login() {
        final String NIM = txtNim.getText().toString().trim();
        final String pass = txtPassword.getText().toString().trim();
        if(NIM.equals("") || pass.equals("")  ) {
            Toast.makeText(LoginActivity.this, "NIM/Password tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else{
            loading = ProgressDialog.show(LoginActivity.this, "Check Data Login", "Wait...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if(response.equals("success")){

                                SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putString(Config.NIM_SHARED_PREF, NIM);
                                editor.putString(Config.password_SHARED_PREF, pass);
                                editor.commit();

                                getUser();
                            }else{

                                customDialog.show();

                                loading.dismiss();
                                Toast.makeText(LoginActivity.this, "NIM/Password Salah", Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String json = null;
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                switch (response.statusCode) {
                                    case 400:
                                        json = new String(response.data);
                                        json = trimMessage(json, "message");
                                        if (json != null) displayMessage(json);
                                        break;
                                }
                            }
                        }
                        public String trimMessage(String json, String key) {
                            String trimmedString = null;
                            try {
                                JSONObject obj = new JSONObject(json);
                                trimmedString = obj.getString(key);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return null;
                            }
                            return trimmedString;
                        }
                        public void displayMessage(String toastString) {
                            Toast.makeText(LoginActivity.this, toastString, Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(Config.KEY_EMP_nim, NIM);
                    params.put(Config.KEY_EMP_password, pass);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    private void showUser() {

        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                 nama_user = jo.getString("nama");
                 alamat = jo.getString("alamat");
                 jurusan = jo.getString("jurusan");
                 foto = jo.getString("foto");
                 semester = jo.getString("semester");
            }
            SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
            editor.putString(Config.nama_SHARED_PREF, nama_user);
            editor.putString(Config.alamat_SHARED_PREF, alamat);
            editor.putString(Config.jurusan_SHARED_PREF, jurusan);
            editor.putString(Config.foto_SHARED_PREF, foto);
            editor.putString(Config.semester_SHARED_PREF, semester);
            editor.commit();

            loading.dismiss();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getUser(){
        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON_STRING = s;
                showUser();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.USER_URL+txtNim.getText().toString());
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }


    private void initCustomDialog(){
        customDialog = new Dialog(this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.popup_salah_password);
        customDialog.setCancelable(true);
    }
}