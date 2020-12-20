package com.stikom.skkm_mahasiswa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class SeminarActivity extends AppCompatActivity {
    ListView lispiu;
    JSONArray result;
    private String JSON_STRING,ID_REF;
    private ProgressDialog loading3;
    SimpleAdapter adapter;
    ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seminar);

        lispiu = (ListView)findViewById(R.id.listview);
        btnBack=(ImageButton)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });


        getJSONSeminar();
    }


    private void showSeminar(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);

                String  ID = jo.getString(Config.KEY_SEMINAR_ID);
                String  JUDUL = jo.getString(Config.KEY_SEMINAR_JUDUL);
                String  DESKRIPSI = jo.getString(Config.KEY_SEMINAR_DESKRIPSI);
                String  PEOPLE = jo.getString(Config.KEY_SEMINAR_PEOPLE);
                String  IMAGE = jo.getString(Config.KEY_SEMINAR_IMAGE);
                String  SEMINARDATE = jo.getString(Config.KEY_SEMINAR_SEMINARDATE);
                String  TANGGAL = jo.getString(Config.KEY_SEMINAR_TANGGAL);


                HashMap<String, String> employees = new HashMap<>();
                employees.put(Config.TAG_SEMINAR_ID, ID);
                employees.put(Config.TAG_SEMINAR_JUDUL, JUDUL);
                employees.put(Config.TAG_SEMINAR_DESKRIPSI, DESKRIPSI);
                employees.put(Config.TAG_SEMINAR_PEOPLE, PEOPLE);
                employees.put(Config.TAG_SEMINAR_IMAGE, IMAGE);
                employees.put(Config.TAG_SEMINAR_SEMINARDATE, SEMINARDATE);
                employees.put(Config.TAG_SEMINAR_TANGGAL, TANGGAL);

                list.add(employees);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new SimpleAdapter(
               SeminarActivity.this, list, R.layout.row_data_seminar,
                new String[]{Config.TAG_SEMINAR_JUDUL, Config.TAG_SEMINAR_SEMINARDATE, Config.TAG_SEMINAR_DESKRIPSI,Config.TAG_SEMINAR_PEOPLE,},
                new int[]{R.id.txtjudul,R.id.txtdate, R.id.txtisi, R.id.txtPerson});

        lispiu.setAdapter(adapter);
        lispiu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
            }
        });
    }


    private void getJSONSeminar(){
        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading3 = ProgressDialog.show(SeminarActivity.this,"Fetching Data","Wait...",false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading3.dismiss();
                JSON_STRING = s;
                showSeminar();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String  s = null;
                s = rh.sendGetRequest(Config.SEMINAR_URL);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}