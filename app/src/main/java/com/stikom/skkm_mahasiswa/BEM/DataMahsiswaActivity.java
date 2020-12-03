package com.stikom.skkm_mahasiswa.BEM;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.stikom.skkm_mahasiswa.BEM.adapter.ListViewAdapterUser;
import com.stikom.skkm_mahasiswa.BEM.adapter.adapterControlUser;
import com.stikom.skkm_mahasiswa.BobotActivity;
import com.stikom.skkm_mahasiswa.Config.Config;
import com.stikom.skkm_mahasiswa.Config.ListViewAdapter;
import com.stikom.skkm_mahasiswa.Config.RequestHandler;
import com.stikom.skkm_mahasiswa.Config.adapterControl;
import com.stikom.skkm_mahasiswa.MainActivity;
import com.stikom.skkm_mahasiswa.R;
import com.stikom.skkm_mahasiswa.SeminarActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DataMahsiswaActivity extends AppCompatActivity {
    ListView lispiu;
    JSONArray result;
    private String JSON_STRING,ID_REF;
    private ProgressDialog loading3;
    SimpleAdapter adapter;
    ListViewAdapterUser listViewAdapterUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_mahsiswa);
        ImageButton btnBack=(ImageButton)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent( DataMahsiswaActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        lispiu = (ListView)findViewById(R.id.listview);

        SearchView searchView = (SearchView)findViewById(R.id.searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)) {
                    listViewAdapterUser.filter("");
                    lispiu.clearTextFilter();
                } else {
                    listViewAdapterUser.filter(s);
                }
                return true;
            }
        });

        getJSONMahasiswa();
    }
    private void showSeminar(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();

        final ArrayList<adapterControlUser> arrayListUser = new ArrayList<>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                adapterControlUser app = new adapterControlUser(
                        jo.getString(Config.TAG_USER_nim),
                        jo.getString(Config.TAG_USER_nama),
                        jo.getString(Config.TAG_USER_jurusan),
                        jo.getString(Config.TAG_USER_level));
                arrayListUser.add(app);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listViewAdapterUser = new ListViewAdapterUser(this, arrayListUser);
        lispiu.setAdapter(listViewAdapterUser);
        lispiu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String nim=arrayListUser.get(position).getnim()+"";
                String nama=arrayListUser.get(position).getnama()+"";
                SharedPreferences sharedPreferences = DataMahsiswaActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Config.NIM_DETAIL_SHARED_PREF, nim);
                editor.putString(Config.NAMA_DETAIL_SHARED_PREF, nama);
                editor.commit();
                Intent intent = new Intent( DataMahsiswaActivity.this, DetailUserActivity.class);
                startActivity(intent);
            }
        });
    }


    private void getJSONMahasiswa(){
        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading3 = ProgressDialog.show(DataMahsiswaActivity.this,"Fetching Data","Wait...",false,false);
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
                s = rh.sendGetRequest(Config.getAllUser);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}