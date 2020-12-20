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
import android.widget.Spinner;

import com.stikom.skkm_mahasiswa.Config.Config;
import com.stikom.skkm_mahasiswa.Config.ListViewAdapter;
import com.stikom.skkm_mahasiswa.Config.RequestHandler;
import com.stikom.skkm_mahasiswa.Config.adapterControl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class SkkmActivity extends AppCompatActivity {

    ListView lispiu;
    JSONArray result;
    private String JSON_STRING,ID_REF;
    private ProgressDialog loading3;
    SimpleAdapter adapter;
    ImageButton btnBack;
    ListViewAdapter listViewAdapter;
    ArrayList<adapterControl> arrayList = new ArrayList<>();
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skkm);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String text = spinner.getSelectedItem().toString();

               if(arrayList.isEmpty()  & text.equals("Semua")) {
                   listViewAdapter.filter("");
               }else{
                if (text.equals("Semua")) {
                    listViewAdapter.filter("");
//                    lispiu.clearTextFilter();

                } else {
                    listViewAdapter.filter(text);
                }

               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        lispiu = (ListView)findViewById(R.id.listview);
        btnBack=(ImageButton)findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getJSONSKKM();

    }
    private void showSKKM()  {
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);

                String date=jo.getString(Config.KEY_SKKM_tanggal);
                SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date newDate= null;
                try {
                    newDate = spf.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                spf= new SimpleDateFormat("dd-MM-yyyy");
                date = spf.format(newDate);
                System.out.println(date);

                adapterControl app = new adapterControl(jo.getString(Config.KEY_SKKM_id),
                                jo.getString(Config.KEY_SKKM_judul),
                                jo.getString(Config.KEY_SKKM_deskripsi),
                                jo.getString(Config.KEY_SKKM_image),
                                jo.getString(Config.KEY_SKKM_seminarDate),
                                date,
                                jo.getString(Config.KEY_SKKM_poin),
                                jo.getString(Config.KEY_SKKM_kategori));
                arrayList.add(app);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

            listViewAdapter = new ListViewAdapter(this, arrayList);
            lispiu.setAdapter(listViewAdapter);

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