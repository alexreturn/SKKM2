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
        getJSONHistory();
        return view;
    }


    private void showEmployee(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);

                String  ID = jo.getString(Config.KEY_EVENT_ID);
                String  JUDUL = jo.getString(Config.KEY_EVENT_JUDUL);
                String  EVENT = jo.getString(Config.KEY_EVENT_EVENT);
                String  EVENTDATE = jo.getString(Config.KEY_EVENT_EVENTDATE);
                String  TANGGAL = jo.getString(Config.KEY_EVENT_TANGGAL);


                HashMap<String, String> employees = new HashMap<>();
                employees.put(Config.TAG_EVENT_ID, ID);
                employees.put(Config.TAG_EVENT_JUDUL, JUDUL);
                employees.put(Config.TAG_EVENT_EVENT, EVENT);
                employees.put(Config.TAG_EVENT_EVENTDATE, EVENTDATE);
                employees.put(Config.TAG_EVENT_TANGGAL, TANGGAL);
                list.add(employees);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new SimpleAdapter(
                getActivity(), list, R.layout.row_data_event,
                new String[]{Config.TAG_EVENT_JUDUL, Config.TAG_EVENT_EVENTDATE, Config.TAG_EVENT_EVENT,},
                new int[]{R.id.txtjudul,R.id.txtdate, R.id.txtisi});

        lispiu.setAdapter(adapter);
        lispiu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
            }
        });
    }


    private void getJSONHistory(){
        System.out.println(ID_REF);
        class GetJSON extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading3 = ProgressDialog.show(getActivity(),"Fetching Data","Wait...",false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading3.dismiss();
                JSON_STRING = s;
                showEmployee();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String  s = null;
                s = rh.sendGetRequest(Config.EVENT_URL);
                 return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}