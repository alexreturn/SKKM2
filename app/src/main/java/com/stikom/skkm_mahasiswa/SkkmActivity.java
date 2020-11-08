package com.stikom.skkm_mahasiswa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.stikom.skkm_mahasiswa.Config.Config;
import com.stikom.skkm_mahasiswa.Config.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SkkmActivity extends AppCompatActivity {

    ListView lispiu;
    JSONArray result;
    private String JSON_STRING,ID_REF;
    private ProgressDialog loading3;
    SimpleAdapter adapter;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skkm);

        lispiu = (ListView)findViewById(R.id.listview);
        btnBack=(ImageButton)findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent( SkkmActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        getJSONSKKM();
    }
    private void showSKKM(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);

                String  ID = jo.getString(Config.KEY_SKKM_id);
                String  JUDUL = jo.getString(Config.KEY_SKKM_judul);
                String  DESKRIPSI = jo.getString(Config.KEY_SKKM_deskripsi);
                String  IMAGE = jo.getString(Config.KEY_SKKM_image);
                String  SEMINARDATE = jo.getString(Config.KEY_SKKM_seminarDate);
                String  TANGGAL = jo.getString(Config.KEY_SKKM_tanggal);
                String  POIN = jo.getString(Config.KEY_SKKM_poin);

                HashMap<String, String> employees = new HashMap<>();
                employees.put(Config.TAG_SKKM_id, ID);
                employees.put(Config.TAG_SKKM_judul, JUDUL);
                employees.put(Config.TAG_SKKM_deskripsi, DESKRIPSI);
                employees.put(Config.TAG_SKKM_image, IMAGE);
                employees.put(Config.TAG_SKKM_seminarDate, SEMINARDATE);
                employees.put(Config.TAG_SKKM_tanggal, TANGGAL);
                employees.put(Config.TAG_SKKM_poin, POIN);

                list.add(employees);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new SimpleAdapter(
                SkkmActivity.this, list, R.layout.row_data_skkm,
                new String[]{Config.TAG_SKKM_tanggal, Config.TAG_SKKM_judul, Config.TAG_SKKM_poin,},
                new int[]{R.id.txttgl,R.id.txtjudul, R.id.txtpoin});

        lispiu.setAdapter(adapter);
        lispiu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
            }
        });
    }


    private void getJSONSKKM(){
        SharedPreferences sharedPreferences = SkkmActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String nim = sharedPreferences.getString(Config.NIM_SHARED_PREF, "null");
        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading3 = ProgressDialog.show(SkkmActivity.this,"Fetching Data","Wait...",false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading3.dismiss();
                JSON_STRING = s;
                showSKKM();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String  s = null;
                s = rh.sendGetRequest(Config.SKKM_URL+nim);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}