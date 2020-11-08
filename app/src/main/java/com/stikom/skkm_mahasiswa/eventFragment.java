package com.stikom.skkm_mahasiswa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.stikom.skkm_mahasiswa.Config.Config;
import com.stikom.skkm_mahasiswa.Config.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class eventFragment extends Fragment {

    ListView lispiu;
    JSONArray result;
    private String JSON_STRING,ID_REF;
    private ProgressDialog loading3;
    SimpleAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        lispiu = (ListView)view.findViewById(R.id.listview);
        return view;
    }

//
//    private void showEmployee(){
//        JSONObject jsonObject = null;
//        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
//
//        try {
//            jsonObject = new JSONObject(JSON_STRING);
//            result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
//
//            for(int i = 0; i<result.length(); i++) {
//                JSONObject jo = result.getJSONObject(i);
//
//                String  ID_USER = jo.getString(Config.KEY_History_CR_id);
//                String  NO_KARTU = jo.getString(Config.KEY_History_CR_no_kartu);
//                String  NO_KARTU2 = jo.getString(Config.KEY_History_CR_no_kartu2);
//                String  IMEI1 = jo.getString(Config.KEY_History_CR_imei1);
//                String  IMEI2 = jo.getString(Config.KEY_History_CR_imei2);
//                String  CHANEL = jo.getString(Config.KEY_History_CR_chanel);
//                String  KATEGORI = jo.getString(Config.KEY_History_CR_kategori);
//                String  TANGGAL  = jo.getString(Config.KEY_History_CR_tanggal);
//
//
//                HashMap<String, String> employees = new HashMap<>();
//                employees.put(Config.TAG_History_Nomor, String.valueOf(i+1));
//                employees.put(Config.TAG_History_CR_id, ID_USER);
//                employees.put(Config.TAG_History_CR_no_kartu, NO_KARTU);
//                employees.put(Config.TAG_History_CR_no_kartu2, NO_KARTU2);
//                employees.put(Config.TAG_History_CR_imei1, IMEI1);
//                employees.put(Config.TAG_History_CR_imei2, IMEI2);
//                employees.put(Config.TAG_History_CR_chanel, CHANEL);
//                employees.put(Config.TAG_History_CR_kategori, KATEGORI);
//                employees.put(Config.TAG_History_CR_tanggal, TANGGAL);
//                list.add(employees);
//            }
//
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        adapter = new SimpleAdapter(
//                getActivity(), list, R.layout.row_data_history_lengkap,
//                new String[]{Config.TAG_History_CR_no_kartu, Config.TAG_History_CR_no_kartu2, Config.TAG_History_CR_imei1, Config.TAG_History_CR_imei2, Config.TAG_History_CR_kategori, Config.TAG_History_CR_chanel, Config.TAG_History_CR_tanggal,Config.TAG_History_Nomor},
//                new int[]{R.id.TextNomor,R.id.TextNomor2, R.id.TextImei1,R.id.TextImei2,R.id.TextKategori,R.id.TextChanel,R.id.TextTanggal,R.id.txtnomor});
//
//        lispiu.setAdapter(adapter);
//        lispiu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView parent, View view, int position, long id) {
//            }
//        });
//    }
//
//
//    private void getJSONHistory(){
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        final String ID_REF = sharedPreferences.getString(Config.ID_SHARED_PREF, "null");
//
//        System.out.println(ID_REF);
//        class GetJSON extends AsyncTask<Void,Void,String> {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                loading3 = ProgressDialog.show(HistoryActivity.this,"Fetching Data","Wait...",false,false);
//            }
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                loading3.dismiss();
//                JSON_STRING = s;
//                showEmployee();
//            }
//
//            @Override
//            protected String doInBackground(Void... params) {
//                RequestHandler rh = new RequestHandler();
//                String  s = null;
//                s = rh.sendGetRequest(Config.get_history3);
//                 return s;
//            }
//        }
//        GetJSON gj = new GetJSON();
//        gj.execute();
//    }
}