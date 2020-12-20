package com.stikom.skkm_mahasiswa;

        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.AsyncTask;
        import android.os.Bundle;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageButton;
        import android.widget.TextView;

        import com.stikom.skkm_mahasiswa.BEM.DataMahsiswaActivity;
        import com.stikom.skkm_mahasiswa.Config.Config;
        import com.stikom.skkm_mahasiswa.Config.RequestHandler;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;

public class homeFragment extends Fragment {

    TextView txtname,txtnim,JumlahSKKM,JumlahPOIN;
    ImageButton btnSeminar,btnSKKM,btnBOBOT,btnMaster;

    private String JSON_STRING;
    String nama_user,alamat,jurusan,nama,nimuser,LEVEL;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
         nama = sharedPreferences.getString(Config.nama_SHARED_PREF, "null");
         nimuser = sharedPreferences.getString(Config.NIM_SHARED_PREF, "null");
         alamat = sharedPreferences.getString(Config.alamat_SHARED_PREF, "null");
         jurusan = sharedPreferences.getString(Config.jurusan_SHARED_PREF, "null");

        JumlahSKKM=(TextView)view.findViewById(R.id.JumlahSKKM);
        JumlahPOIN=(TextView)view.findViewById(R.id.JumlahPOIN);

        txtname=(TextView)view.findViewById(R.id.txtnama);
        txtnim=(TextView)view.findViewById(R.id.txtnim);

        txtname.setText(nama);
        txtnim.setText(nimuser+" - "+jurusan);


        btnSeminar=(ImageButton)view.findViewById(R.id.btnSeminar);
        btnSKKM=(ImageButton)view.findViewById(R.id.btnSKKM);
        btnBOBOT=(ImageButton)view.findViewById(R.id.btnBOBOT);
        btnMaster=(ImageButton)view.findViewById(R.id.btnMaster);


        btnMaster.setVisibility(View.INVISIBLE);
        btnMaster.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getContext(), DataMahsiswaActivity.class);
                startActivity(intent);
            }
        });
        btnSeminar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getContext(), SeminarActivity.class);
                startActivity(intent);
            }
        });
        btnSKKM.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getContext(), SkkmActivity.class);
                startActivity(intent);
            }
        });
        btnBOBOT.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent( getContext(), BobotActivity.class);
                startActivity(intent);
            }
        });
        getUser();
        return view;
    }

    private void showUser() {

        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONArray result2 = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY2);

            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                nama_user = jo.getString("nama");
                alamat = jo.getString("alamat");
                jurusan = jo.getString("jurusan");
                LEVEL = jo.getString("level");

                txtname.setText(nama_user);
                txtnim.setText(nimuser+" - "+jurusan);
                if(LEVEL.equals("BEM")){
                    btnMaster.setVisibility(View.VISIBLE);
                }else{
                    btnMaster.setVisibility(View.GONE);
                }

//                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//               editor.putString(Config.nama_SHARED_PREF, nama_user);
//                editor.putString(Config.alamat_SHARED_PREF, alamat);
//                editor.putString(Config.jurusan_SHARED_PREF, jurusan);
//                editor.commit();
            }


            for (int i = 0; i < result2.length(); i++) {
                JSONObject jo = result2.getJSONObject(i);
                String jmlskkm = jo.getString("jumlah");
                String  jmlpoin = jo.getString("JumlahPOIN");

                JumlahSKKM.setText(jmlskkm);
                if(jmlpoin.equals(null) || jmlpoin.equals("null")){

                    JumlahPOIN.setText("0");
                }else{

                    JumlahPOIN.setText(jmlpoin);
                }

            }


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
                String s = rh.sendGetRequest(Config.USER_URL+nimuser);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}